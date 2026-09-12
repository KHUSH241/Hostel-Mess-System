package com.hms.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Thin wrapper around simple pipe-delimited text files that act as our
 * persistence layer, so we don't need an external database dependency
 * to run the project from the command line.
 */
public final class FileStorageUtil {

    private FileStorageUtil() { }

    public static List<String> readLines(String path) {
        List<String> lines = new ArrayList<>();
        Path p = Paths.get(path);
        if (!Files.exists(p)) {
            return lines;
        }
        try {
            for (String line : Files.readAllLines(p)) {
                if (!line.isBlank()) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: could not read " + path + " -> " + e.getMessage());
        }
        return lines;
    }

    public static void appendLine(String path, String line) {
        ensureParentDir(path);
        try {
            Files.writeString(Paths.get(path), line + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Warning: could not write to " + path + " -> " + e.getMessage());
        }
    }

    public static void overwriteAll(String path, List<String> lines) {
        ensureParentDir(path);
        try {
            Files.write(Paths.get(path), lines,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Warning: could not rewrite " + path + " -> " + e.getMessage());
        }
    }

    private static void ensureParentDir(String path) {
        try {
            Path parent = Paths.get(path).getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException ignored) {
        }
    }
}
