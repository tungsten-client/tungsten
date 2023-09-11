package org.tungsten.client.util;

import lombok.SneakyThrows;

import javax.tools.*;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClassFileCompiler {
	static final JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();

	@SneakyThrows
	public static CompilationResults compile(Path input, List<String> additionalArgs) {
		Path tempDirectory = Files.createTempDirectory("tungsten-compiler");

		DiagnosticCollector<JavaFileObject> jfs = new DiagnosticCollector<>();
		StandardJavaFileManager standardFileManager = systemJavaCompiler.getStandardFileManager(jfs, null, null);
		Iterable<? extends JavaFileObject> javaFileObjectsFromPaths = standardFileManager.getJavaFileObjectsFromPaths(
				List.of(input)
		);
		StringWriter sw = new StringWriter();
		List<String> compilerArgs = new ArrayList<>(2 + additionalArgs.size());
		compilerArgs.addAll(additionalArgs);
		compilerArgs.addAll(List.of("-d", tempDirectory.toAbsolutePath().toString()));
		boolean aBoolean = systemJavaCompiler.getTask(sw, standardFileManager, jfs, compilerArgs, null,
				javaFileObjectsFromPaths).call();
		if (!aBoolean) {
			return new CompilationResults(jfs.getDiagnostics(), sw.toString(), false, null);
		}
		try (Stream<Path> p = Files.walk(tempDirectory)) {
			List<Path> list = p.filter(Files::isRegularFile).filter(a->a.getFileName().toString().equals(input.getFileName().toString().replaceFirst("(?s).java(?!.*?.java)", "") + ".class")).toList();
			if (list.size() != 1) {
				// I intentionally did not delete the temp directory here because one might want to look into it when this happens
				throw new IllegalStateException(
						"Compiler generated " + list.size() + " source files instead of one. See output directory @ " + tempDirectory.toAbsolutePath());
			}
			Path path = list.get(0);
			byte[] ba = Files.readAllBytes(path);
			return new CompilationResults(jfs.getDiagnostics(), sw.toString(), true, ba);
		}
	}

	public record CompilationResults(List<Diagnostic<? extends JavaFileObject>> diagnostics, String logs,
									 boolean compiledSuccessfully, byte[] compiledClassFile) {
	}
}