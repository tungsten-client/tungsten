package org.tungsten.client.util.io;

import lombok.SneakyThrows;
import org.tungsten.client.Tungsten;
import org.tungsten.client.initializer.ItemInitializer;
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

public class ItemCompiler {

    public static void compileItems() {
        Utils.ensureDirectoryIsCreated(Tungsten.RUNDIR.resolve("items"));
        searchAndCompileItems(Tungsten.RUNDIR.resolve("items"));
    }


    @SneakyThrows
    private static void searchAndCompileItems(Path path) {
        Utils.rmDirectoryTree(ItemInitializer.ITEMS_COMPILED);
        Utils.ensureDirectoryIsCreated(ItemInitializer.ITEMS_COMPILED);
        try (Stream<Path> r = Files.walk(path)) {
            r.filter(path1 -> Files.isRegularFile(path1) && path1.toString().endsWith(".java")).forEach(path1 -> {
                try {
                    compileItem(path1);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static void compileItem(Path item) throws IOException {
        Tungsten.LOGGER.info("compileItem called on " + item.toAbsolutePath());
//		LibraryDownloader.ensurePresent();

        String fileName = item.getFileName().toString();
        Path output = ItemInitializer.ITEMS_COMPILED.resolve(
                fileName.substring(0, fileName.length() - ".java".length()) + ".class");

        String libraries = LibraryDownloader.generateClasspath();

        ClassFileCompiler.CompilationResults compile = ClassFileCompiler.compile(item, List.of("-cp", libraries));
        if (compile.compiledSuccessfully()) {
            Files.write(output, compile.compiledClassFile());
        } else {
            Tungsten.LOGGER.error("Item {} failed to compile", item.toAbsolutePath());
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
