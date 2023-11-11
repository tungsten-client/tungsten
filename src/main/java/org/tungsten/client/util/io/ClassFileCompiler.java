package org.tungsten.client.util.io;

import org.tungsten.client.Tungsten;

import javax.tools.*;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ClassFileCompiler {
	static final JavaCompiler systemJavaCompiler = ToolProvider.getSystemJavaCompiler();

	public static CompilationResults compile(Path input, List<String> additionalArgs) throws IOException {
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
			return new CompilationResults(jfs.getDiagnostics(), sw.toString(), false, null, null);
		}
		try (Stream<Path> p = Files.walk(tempDirectory)) {
			List<Path> list = p.filter(Files::isRegularFile).filter(a->a.getFileName().toString().contains(input.getFileName().toString().replaceFirst("(?s).java(?!.*?.java)", ""))).toList();
//			if (list.size() != 1) {
//				// I intentionally did not delete the temp directory here because one might want to look into it when this happens
//				throw new IllegalStateException(
//						"Compiler generated " + list.size() + " source files instead of one. See output directory @ " + tempDirectory.toAbsolutePath());
//			}
			ArrayList<String> names = new ArrayList<>();
			ArrayList<byte[]> files = new ArrayList<>();
			Tungsten.LOGGER.info(list.size() + " FILES TO COMPILE");
			for(Path path : list) {
				String spath = path.toString();
				Tungsten.LOGGER.info("COMPILING:" + spath);
				files.add(Files.readAllBytes(path));
				names.add(spath.substring(spath.lastIndexOf("\\") + 1));
			}
			return new CompilationResults(jfs.getDiagnostics(), sw.toString(), true, names, files);
		}
	}

	public record CompilationResults(List<Diagnostic<? extends JavaFileObject>> diagnostics, String logs,
									 boolean compiledSuccessfully, ArrayList<String> compiledClassFileNames, ArrayList<byte[]> compiledClassFiles) {
	}
}
