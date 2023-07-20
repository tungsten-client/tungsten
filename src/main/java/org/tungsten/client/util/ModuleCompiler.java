package org.tungsten.client.util;

import lombok.SneakyThrows;
import org.tungsten.client.Tungsten;
import org.tungsten.client.initializer.ModuleInitializer;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class ModuleCompiler {

	//declare the mapped minecraft sources, the unmapped minecraft sources, and the tungsten jar file, the three things needed to call the java compiler and make everything work
	//waawaa shut up NERD!!!!
//    private static File mapped;
//    private static File unmapped;
//    private static File self;
//
//    static {
//        mapped = new File(Tungsten.LIBS, "minecraft-mapped.jar");
//        unmapped = new File(Tungsten.LIBS, "minecraft-unmapped.jar");
//        self = new File(Tungsten.LIBS, "Tungsten-dev.jar");
//    }

	public static void compileModules() {
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

		String fileName = module.getFileName().toString();
		Path output = ModuleInitializer.MODULES_COMPILED.resolve(
				fileName.substring(0, fileName.length() - ".java".length()) + ".class");

		
		//ty crosby you saved me from writing a method to download the libs required
		String libraries = System.getProperty("java.class.path"); //mapped.toAbsolutePath() + File.pathSeparator + unmapped.toAbsolutePath() + File.pathSeparator + self.toAbsolutePath();

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


	public static void setupCompilerEnvironment() throws IOException {
        if (!Files.exists(mapped)) {
            WebUtils.downloadURLToPath(
                    "https://cdn.discordapp.com/attachments/1121169365883166790/1121169525522563242/minecraft-mapped.jar",
                    mapped);
        }
        if (!Files.exists(unmapped)) {
            WebUtils.downloadURLToPath(
                    "https://cdn.discordapp.com/attachments/1121169365883166790/1121169526021689344/minecraft-unmapped.jar",
                    unmapped);
        }
		if (!Files.exists(self)) {
			WebUtils.downloadURLToPath(
					"https://cdn.discordapp.com/attachments/1121169365883166790/1130395140884791296/Tungsten-dev.jar",
					self);
		}
	}

}
