
package com.typing.service;

import com.typing.model.TestDifficulty;
import com.typing.model.TestResult;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {

    private final Path filePath;

    public FileStorage() {
        this(Paths.get("typing-history.csv"));
    }

    public FileStorage(Path filePath) {
        this.filePath = filePath;
    }

    public synchronized void save(TestResult result) throws IOException {
        if (result == null) {
            return;
        }

        Path parent = filePath.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        boolean needsHeader = !Files.exists(filePath)
                || Files.size(filePath) == 0;

        try (BufferedWriter writer = Files.newBufferedWriter(
                filePath,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {

            if (needsHeader) {
                writer.write("id,timestamp,duration,difficulty,wpm,accuracy,errors,characters,score,grade");
                writer.newLine();
            }

            writer.write(String.join(",",
                    encode(result.getId()),
                    encode(String.valueOf(result.getTimestamp())),
                    String.valueOf(result.getDurationSeconds()),
                    encode(result.getDifficulty() == null
                            ? "" : result.getDifficulty().name()),
                    String.valueOf(result.getWpm()),
                    String.valueOf(result.getAccuracy()),
                    String.valueOf(result.getErrors()),
                    String.valueOf(result.getCharactersTyped()),
                    String.valueOf(result.getScore()),
                    encode(result.getGrade())));
            writer.newLine();
        }
    }

    public synchronized List<TestResult> loadAll() throws IOException {
        List<TestResult> results = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return results;
        }

        try (BufferedReader reader = Files.newBufferedReader(
                filePath, StandardCharsets.UTF_8)) {

            String line;
            boolean firstLine = true;

            while ((line = reader.readLine()) != null) {
                if (firstLine && line.startsWith("id,")) {
                    firstLine = false;
                    continue;
                }
                firstLine = false;

                try {
                    List<String> fields = parseCsvLine(line);
                    if (fields.size() != 10) {
                        continue;
                    }

                    String id = fields.get(0);
                    LocalDateTime timestamp =
                            LocalDateTime.parse(fields.get(1));
                    int duration = Integer.parseInt(fields.get(2));

                    TestDifficulty difficulty = fields.get(3).isEmpty()
                            ? null
                            : TestDifficulty.valueOf(fields.get(3));

                    int wpm = Integer.parseInt(fields.get(4));
                    double accuracy = Double.parseDouble(fields.get(5));
                    int errors = Integer.parseInt(fields.get(6));
                    int characters = Integer.parseInt(fields.get(7));
                    int score = Integer.parseInt(fields.get(8));
                    String grade = fields.get(9);

                    results.add(new TestResult(
                            id, timestamp, duration, difficulty, wpm,
                            accuracy, errors, characters, score, grade));

                } catch (RuntimeException ex) {
                    System.err.println(
                            "Skipping invalid history record: " + ex.getMessage());
                }
            }
        }

        return results;
    }

    public synchronized void clear() throws IOException {
        if (Files.exists(filePath)) {
            Files.delete(filePath);
        }
    }

    private String encode(String value) {
        if (value == null) {
            return "\"\"";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }

    private List<String> parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean insideQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);

            if (ch == '"') {
                if (insideQuotes && i + 1 < line.length()
                        && line.charAt(i + 1) == '"') {
                    field.append('"');
                    i++;
                } else {
                    insideQuotes = !insideQuotes;
                }
            } else if (ch == ',' && !insideQuotes) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }

        fields.add(field.toString());
        return fields;
    }
}
