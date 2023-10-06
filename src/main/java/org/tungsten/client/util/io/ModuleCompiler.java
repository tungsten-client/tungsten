package org.tungsten.client.util.io;

import lombok.SneakyThrows;
import org.tungsten.client.Tungsten;
import org.tungsten.client.initializer.ModuleInitializer;
import org.tungsten.client.util.Utils;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class ModuleCompiler {

	public static void compileModules() {
		Utils.ensureDirectoryIsCreated(Tungsten.RUNDIR.resolve("modules"));
		searchAndCompileModules(Tungsten.RUNDIR.resolve("modules"));
	}


	@SneakyThrows
	private static void searchAndCompileModules(Path path) {
		Utils.rmDirectoryTree(ModuleInitializer.MODULES_COMPILED);
		Utils.ensureDirectoryIsCreated(ModuleInitializer.MODULES_COMPILED);
		try (Stream<Path> r = Files.walk(path)) {
			r.filter(path1 -> Files.isRegularFile(path1) && path1.toString().endsWith(".java")).forEach(path1 -> {
				try {
					compileModule(path1);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
		}
	}

	private static void compileModule(Path module) throws IOException {
		Tungsten.LOGGER.info("compileModule called on " + module.getFileName().toString());
//		LibraryDownloader.ensurePresent();

		String fileName = module.getFileName().toString();
		Path output = ModuleInitializer.MODULES_COMPILED.resolve(
				fileName.substring(0, fileName.length() - ".java".length()) + ".class");


		//ty crosby you saved me from writing a method to download the libs required
		//nevermind saturn retardo brain
		//why is this MY fault joe? you didn't even test your code!

		String libraries = LibraryDownloader.generateClasspath();

		ClassFileCompiler.CompilationResults compile = ClassFileCompiler.compile(module, List.of("-cp", libraries));
		if (compile.compiledSuccessfully()) {
			Files.write(output, compile.compiledClassFile());
		} else {
			Tungsten.LOGGER.error("Module {} failed to compile", module.toAbsolutePath());
			for (Diagnostic<? extends JavaFileObject> diagnostic : compile.diagnostics()) {
				Tungsten.LOGGER.error("  ...: [{}] {}: {}. At {}:{} in {}", diagnostic.getKind(), diagnostic.getCode(),
						diagnostic.getMessage(
								Locale.getDefault()), diagnostic.getLineNumber(), diagnostic.getColumnNumber(),
						diagnostic.getSource());
			}
			Tungsten.LOGGER.error("Javac output:");
			Arrays.stream(compile.logs().split("\n")).map(s -> "  ...:" + s).forEach(Tungsten.LOGGER::error);
		}
	}



}
