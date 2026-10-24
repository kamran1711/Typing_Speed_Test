package com.typing.model;

/**
 * Represents difficulty tiers for typing tests.
 * Prepared for Member 2 (Typing Engine &amp; Word Generators).
 */
public enum TestDifficulty {
    EASY("Easy", "Common vocabulary and basic punctuation", "badge-easy"),
    MEDIUM("Medium", "Varied sentences with numbers and punctuation", "badge-medium"),
    HARD("Hard", "Advanced vocabulary, symbols, and technical prose", "badge-hard");

    private final String displayName;
    private final String description;
    private final String styleClass;

    TestDifficulty(String displayName, String description, String styleClass) {
        this.displayName = displayName;
        this.description = description;
        this.styleClass = styleClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getStyleClass() {
        return styleClass;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
