package org.tungsten.client.util;

import lombok.SneakyThrows;
import org.tungsten.client.Tungsten;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {

	private static final int[] EXPECTED_CLASS_SIGNATURE = new int[] { 0xCA, 0xFE, 0xBA, 0xBE };

	public static boolean checkSignature(byte[] classFile) {
		byte[] cSig = Arrays.copyOfRange(classFile, 0, 4);
		int[] cSigP = new int[4];
		for (int i = 0; i < cSig.length; i++) {
			cSigP[i] = Byte.toUnsignedInt(cSig[i]);
		}
		return !Arrays.equals(cSigP, EXPECTED_CLASS_SIGNATURE);
	}

	@SneakyThrows
	public static boolean isDirectoryEmpty(Path directory) {
        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Not a directory: " + directory.toAbsolutePath());
        }
		try (Stream<Path> p = Files.list(directory)) {
			return p.findAny().isEmpty();
		}
	}

	@SneakyThrows
	public static void ensureDirectoryIsCreated(Path directory) {
        if (Files.exists(directory) && !Files.isDirectory(directory)) {
            Files.delete(directory);
        }
        if (!Files.exists(directory)) {
            Files.createDirectory(directory);
        }
	}

	public static void deleteFilesExcept(File directory, String... filenames) {

		if (!directory.isDirectory()) {
			Tungsten.LOGGER.warn("Invalid directory path: " + directory.getAbsolutePath());
			return;
		}

		File[] files = directory.listFiles();

		if (files == null) {
			Tungsten.LOGGER.error("An error occurred while retrieving files from the directory.");
			return;
		}

		for (File file : files) {
			if (file.isFile() && !arrayContains(filenames, file.getName())) {
				file.delete();
			}
		}
	}

	@SneakyThrows
	public static void rmDirectoryTree(Path directory) {
		if (!Files.isDirectory(directory)) {
			Tungsten.LOGGER.warn("Invalid directory path: " + directory.toAbsolutePath());
			return;
		}
		Files.walkFileTree(directory, new FileVisitor<>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) {
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public static void unzip(Path zipFile, Path folder) throws IOException {
		byte[] buffer = new byte[1024];
		ensureDirectoryIsCreated(folder);

		try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipFile))) {
			ZipEntry ze;
			while ((ze = zipInputStream.getNextEntry()) != null) {
				String entryName = ze.getName();
				Path p = folder.resolve(entryName).toAbsolutePath().normalize();
				if (!p.startsWith(folder.toAbsolutePath().normalize())) {
					throw new IllegalStateException(
							"Probable path traversal attempt: Zip entry '" + entryName + "' in zip file '" + zipFile.toAbsolutePath() + "' resolved to '" + p + "'");
				}
				if (!ze.isDirectory()) {
					Files.createDirectories(p.getParent());
					try (OutputStream fout = Files.newOutputStream(p)) {
						int c;
						while ((c = zipInputStream.read(buffer)) > 0) {
							fout.write(buffer, 0, c);
						}
					}
				}
				zipInputStream.closeEntry();
			}
		}
	}

	private static <T> boolean arrayContains(T[] array, T element) {
		return Arrays.asList(array).contains(element);
	}
}