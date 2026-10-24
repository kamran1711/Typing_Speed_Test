package com.typing.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the completed test record.
 * Designed with clean accessors for UI binding, and fully compatible with Member 3's persistence layer.
 */
public class TestResult {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");

    private String id;
    private LocalDateTime timestamp;
    private int durationSeconds;
    private TestDifficulty difficulty;
    private int wpm;
    private double accuracy; // percentage, e.g. 98.5
    private int errors;
    private int charactersTyped;
    private int score;
    private String grade;

    public TestResult() {
        this.timestamp = LocalDateTime.now();
    }

    public TestResult(String id, LocalDateTime timestamp, int durationSeconds, TestDifficulty difficulty,
                      int wpm, double accuracy, int errors, int charactersTyped, int score, String grade) {
        this.id = id;
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
        this.durationSeconds = durationSeconds;
        this.difficulty = difficulty;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.errors = errors;
        this.charactersTyped = charactersTyped;
        this.score = score;
        this.grade = grade;
    }

    // Convenience constructor calculating score and grade automatically if omitted
    public static TestResult create(String id, int durationSeconds, TestDifficulty difficulty,
                                   int wpm, double accuracy, int errors, int charactersTyped) {
        int calculatedScore = (int) Math.round((wpm * 10.0) * (accuracy / 100.0) - (errors * 5.0));
        if (calculatedScore < 0) calculatedScore = 0;

        String calculatedGrade;
        if (wpm >= 90 && accuracy >= 97.0) calculatedGrade = "S";
        else if (wpm >= 75 && accuracy >= 95.0) calculatedGrade = "A+";
        else if (wpm >= 60 && accuracy >= 92.0) calculatedGrade = "A";
        else if (wpm >= 45 && accuracy >= 88.0) calculatedGrade = "B";
        else calculatedGrade = "C";

        return new TestResult(id, LocalDateTime.now(), durationSeconds, difficulty,
                wpm, accuracy, errors, charactersTyped, calculatedScore, calculatedGrade);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public TestDifficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(TestDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getWpm() {
        return wpm;
    }

    public void setWpm(int wpm) {
        this.wpm = wpm;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getCharactersTyped() {
        return charactersTyped;
    }

    public void setCharactersTyped(int charactersTyped) {
        this.charactersTyped = charactersTyped;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // UI Formatting Helpers for TableView columns
    public String getFormattedDate() {
        return timestamp != null ? timestamp.format(DATE_FORMAT) : "N/A";
    }

    public String getModeString() {
        return durationSeconds + "s • " + (difficulty != null ? difficulty.getDisplayName() : "Standard");
    }

    public String getWpmString() {
        return wpm + " WPM";
    }

    public String getAccuracyString() {
        return String.format("%.1f%%", accuracy);
    }

    public String getDurationString() {
        return durationSeconds + "s";
    }

    public String getDifficultyString() {
        return difficulty != null ? difficulty.getDisplayName() : "Medium";
    }

    public String getScoreString() {
        return score + " pts";
    }
}
