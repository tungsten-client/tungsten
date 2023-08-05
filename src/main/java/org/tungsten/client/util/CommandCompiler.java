package org.tungsten.client.util;

import lombok.SneakyThrows;
import org.tungsten.client.Tungsten;
import org.tungsten.client.initializer.ModuleInitializer;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class CommandCompiler {

	public static void compileCommands() {
		Utils.ensureDirectoryIsCreated(Tungsten.RUNDIR.resolve("commands"));
		searchAndCompileCommands(Tungsten.RUNDIR.resolve("commands"));
	}


	@SneakyThrows
	private static void searchAndCompileCommands(Path path) {
		Utils.rmDirectoryTree(ModuleInitializer.MODULES_COMPILED);
		if (path != null) {
			try (Stream<Path> p = Files.walk(path)) {
				p.filter(path1 -> Files.isRegularFile(path1) && path1.toString().endsWith(".java")).forEach(path1 -> {
					try {
						compileCommand(path1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				});
			}
		}
	}

	private static void compileCommand(Path command) throws IOException {
		Tungsten.LOGGER.info("compileCommand called on " + command.toAbsolutePath());
		LibraryDownloader.ensurePresent();

		String fileName = command.getFileName().toString();
		Path output = ModuleInitializer.MODULES_COMPILED.resolve(
				fileName.substring(0, fileName.length() - ".java".length()) + ".class");

		String libraries = LibraryDownloader.generateClasspath();

		ClassFileCompiler.CompilationResults compile = ClassFileCompiler.compile(command, List.of("-cp", libraries));
		if (compile.compiledSuccessfully()) {
			Files.write(output, compile.compiledClassFile());
		} else {
			Tungsten.LOGGER.error("Command {} failed to compile", command.toAbsolutePath());
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


//    public static void setupCompilerEnvironment() throws IOException, URISyntaxException {
//        WebUtils.downloadURLToPath("https://cdn.discordapp.com/attachments/1121169365883166790/1121169525522563242/minecraft-mapped.jar", mapped);
//        WebUtils.downloadURLToPath("https://cdn.discordapp.com/attachments/1121169365883166790/1121169526021689344/minecraft-unmapped.jar", unmapped);
//    }

}
