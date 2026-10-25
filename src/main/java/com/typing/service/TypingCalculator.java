
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

    public static int calculatePerformanceScore(int wpm, double accuracy, int errors) {
        return calculatePerformanceScore(wpm, accuracy, errors, 60);
    }

    public static int calculatePerformanceScore(int wpm, double accuracy, int errors, int elapsedSeconds) {
        if (wpm < 0) wpm = 0;
        if (accuracy < 0.0) accuracy = 0.0;
        if (errors < 0) errors = 0;
        if (elapsedSeconds <= 0) elapsedSeconds = 1;

        double wpmScore = Math.min(100.0, (wpm / 80.0) * 100.0);
        double accuracyScore = Math.min(100.0, accuracy);
        double consistencyScore = Math.max(0.0, 100.0 - (errors * 3.0));
        double timeFactor = Math.min(1.0, 60.0 / elapsedSeconds);

        double overallScore = (wpmScore * 0.50)
                + (accuracyScore * 0.35)
                + (consistencyScore * 0.10)
                + (timeFactor * 5.0);

        return (int) Math.round(Math.max(0.0, Math.min(100.0, overallScore)));
    }

    public static String classifyTypingPerformance(int score) {
        return classifyTypingPerformance((double) score);
    }

    public static String classifyTypingPerformance(double score) {
        if (score >= 90) return "Excellent";
        if (score >= 75) return "Good";
        if (score >= 60) return "Average";
        if (score >= 40) return "Needs Improvement";
        return "Beginner";
    }

    public static String generateFeedback(int wpm, double accuracy, int errors) {
        StringBuilder feedback = new StringBuilder();

        if (accuracy < 85.0) {
            feedback.append("Focus on accuracy first; slow down to reduce mistakes and keep each keystroke controlled.");
        } else if (wpm >= 60) {
            feedback.append("Strong balance of speed and accuracy. Maintain your rhythm and keep your hands relaxed.");
        } else {
            feedback.append("Good start. Increase your pace gradually while keeping your errors under control.");
        }

        if (errors > 8) {
            feedback.append(" Try to reduce corrections by checking the text before pressing the next key.");
        } else if (wpm < 30) {
            feedback.append(" Practice short timed drills to build fluency and finger coordination.");
        } else {
            feedback.append(" Keep practicing with consistent bursts of typing to build stamina.");
        }

        return feedback.toString();
    }

    public static String comparePerformance(int currentScore, int previousScore) {
        int difference = currentScore - previousScore;

        if (difference > 0) {
            return "Improved by " + difference + " points compared to your previous result. Great progress!";
        }
        if (difference < 0) {
            return "Dropped by " + Math.abs(difference) + " points compared to your previous result. Focus on accuracy and consistency.";
        }
        return "Performance is consistent with your previous result.";
    }

    public static String comparePerformance(double currentScore, double previousScore) {
        return comparePerformance((int) Math.round(currentScore), (int) Math.round(previousScore));
    }

    public static String compareCurrentAndPreviousPerformance(int currentScore, int previousScore) {
        return comparePerformance(currentScore, previousScore);
    }

    public static String compareCurrentAndPreviousPerformance(double currentScore, double previousScore) {
        return comparePerformance(currentScore, previousScore);
    }
}
