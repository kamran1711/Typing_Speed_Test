
package com.typing.service;

public class TypingCalculator {

    private TypingCalculator() {
        // Utility class
    }

    public static int countErrors(String typedText, String targetText) {
        if (typedText == null) typedText = "";
        if (targetText == null) targetText = "";

        int errors = 0;
        int length = Math.min(typedText.length(), targetText.length());

        for (int i = 0; i < length; i++) {
            if (typedText.charAt(i) != targetText.charAt(i)) {
                errors++;
            }
        }

        errors += Math.max(0, typedText.length() - targetText.length());
        return errors;
    }

    public static int countCorrectCharacters(String typedText, String targetText) {
        if (typedText == null) typedText = "";
        return Math.max(0, typedText.length()
                - countErrors(typedText, targetText));
    }

    public static double calculateAccuracy(int correctCharacters,
                                           int totalCharacters) {
        if (totalCharacters <= 0) return 100.0;

        return Math.max(0.0, Math.min(100.0,
                correctCharacters * 100.0 / totalCharacters));
    }

    public static int calculateWpm(int correctCharacters, int elapsedSeconds) {
        if (elapsedSeconds <= 0 || correctCharacters <= 0) return 0;

        return (int) Math.round(
                (correctCharacters / 5.0) / (elapsedSeconds / 60.0));
    }
}
