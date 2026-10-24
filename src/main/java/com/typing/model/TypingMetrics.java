package com.typing.model;

/**
 * Real-time metric snapshot during an active typing test.
 * Used for live HUD binding and progress updates.
 */
public class TypingMetrics {
    private int elapsedSeconds;
    private int remainingSeconds;
    private int wpm;
    private double accuracy; // percentage 0-100
    private int errorCount;
    private int charactersTyped;
    private int totalCharacters;
    private double progress; // 0.0 - 1.0

    public TypingMetrics() {
    }

    public TypingMetrics(int elapsedSeconds, int remainingSeconds, int wpm, double accuracy,
                         int errorCount, int charactersTyped, int totalCharacters, double progress) {
        this.elapsedSeconds = elapsedSeconds;
        this.remainingSeconds = remainingSeconds;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.errorCount = errorCount;
        this.charactersTyped = charactersTyped;
        this.totalCharacters = totalCharacters;
        this.progress = progress;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setElapsedSeconds(int elapsedSeconds) {
        this.elapsedSeconds = elapsedSeconds;
    }

    public int getRemainingSeconds() {
        return remainingSeconds;
    }

    public void setRemainingSeconds(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
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

    public int getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(int errorCount) {
        this.errorCount = errorCount;
    }

    public int getCharactersTyped() {
        return charactersTyped;
    }

    public void setCharactersTyped(int charactersTyped) {
        this.charactersTyped = charactersTyped;
    }

    public int getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(int totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }
}
