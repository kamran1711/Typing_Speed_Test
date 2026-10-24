package com.typing.model;

/**
 * Supported test duration presets.
 */
public enum TestDuration {
    THIRTY_SEC(30, "30s", "Quick Sprint (30 Seconds)"),
    SIXTY_SEC(60, "60s", "Standard Benchmark (60 Seconds)"),
    TWO_MIN(120, "120s", "Endurance Test (2 Minutes)");

    private final int seconds;
    private final String label;
    private final String description;

    TestDuration(int seconds, String label, String description) {
        this.seconds = seconds;
        this.label = label;
        this.description = description;
    }

    public int getSeconds() {
        return seconds;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return label;
    }
}
