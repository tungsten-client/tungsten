package org.tungsten.client.util.io;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Stream-like file tree walker utility.
 * @author Crosby
 */
public record FileTraveller(Path path) {
    private static final FileTraveller EMPTY = new FileTraveller(null);

    public static FileTraveller fromHome() {
        return new FileTraveller(Paths.get(System.getProperty("user.home")));
    }

    public static FileTraveller from(String dir, String... dirs) {
        return new FileTraveller(Paths.get(dir, dirs));
    }

    public static FileTraveller from(Path path) {
        return new FileTraveller(path);
    }

    public FileTraveller then(String dir) {
        return isValid() ? new FileTraveller(this.path.resolve(dir)) : EMPTY;
    }

    public FileTraveller then(String... dirs) {
        if (!isValid()) return EMPTY;
        Path p = this.path;
        for (String dir : dirs) p = p.resolve(dir);
        return new FileTraveller(p);
    }

    public FileTraveller find() {
        if (!isValid()) return EMPTY;
        try (Stream<Path> dirs = Files.list(this.path)) {
            return dirs.findAny().map(FileTraveller::new).orElse(EMPTY);
        } catch (IOException e) {
            return EMPTY;
        }
    }

    public FileTraveller find(Predicate<Path> predicate) {
        if (!isValid()) return EMPTY;
        try (Stream<Path> dirs = Files.list(this.path)) {
            return dirs.filter(predicate).findAny().map(FileTraveller::new).orElse(EMPTY);
        } catch (IOException e) {
            return EMPTY;
        }
    }

    @Nullable
    public Path get() {
        return this.path;
    }

    public Optional<Path> toOptional() {
        return Optional.ofNullable(this.path);
    }

    // Helper methods

    private boolean isValid() {
        return this != EMPTY && Files.exists(this.path);
    }
}
