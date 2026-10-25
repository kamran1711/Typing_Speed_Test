
package com.typing.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypingCalculatorTest {

    @Test
    void shouldCountErrors() {
        assertEquals(1, TypingCalculator.countErrors("hallo", "hello"));
    }

    @Test
    void shouldCountCorrectCharacters() {
        assertEquals(4,
                TypingCalculator.countCorrectCharacters("hallo", "hello"));
    }

    @Test
    void shouldCalculateAccuracy() {
        assertEquals(80.0,
                TypingCalculator.calculateAccuracy(8, 10), 0.001);
    }

    @Test
    void shouldCalculateWpm() {
        assertEquals(20, TypingCalculator.calculateWpm(100, 60));
    }

    @Test
    void shouldReturnZeroWpmWhenTimeIsZero() {
        assertEquals(0, TypingCalculator.calculateWpm(100, 0));
    }

    @Test
    void shouldReturnFullAccuracyForNoCharacters() {
        assertEquals(100.0,
                TypingCalculator.calculateAccuracy(0, 0), 0.001);
    }

    @Test
    void shouldCalculateWpmForThirtySeconds() {
        assertEquals(20, TypingCalculator.calculateWpm(50, 30));
    }

    @Test
    void shouldReturnZeroWpmWhenNoCharactersAreCorrect() {
        assertEquals(0, TypingCalculator.calculateWpm(0, 60));
    }

    @Test
    void shouldCalculatePerformanceScoreOutOfHundred() {
        int score = TypingCalculator.calculatePerformanceScore(60, 95.0, 4, 60);
        assertTrue(score >= 0 && score <= 100);
        assertEquals(85, score);
    }

    @Test
    void shouldClassifyTypingPerformanceIntoLevels() {
        assertEquals("Excellent", TypingCalculator.classifyTypingPerformance(95));
        assertEquals("Good", TypingCalculator.classifyTypingPerformance(80));
        assertEquals("Average", TypingCalculator.classifyTypingPerformance(65));
        assertEquals("Needs Improvement", TypingCalculator.classifyTypingPerformance(45));
        assertEquals("Beginner", TypingCalculator.classifyTypingPerformance(20));
    }

    @Test
    void shouldGenerateHelpfulFeedbackAndSuggestions() {
        String feedback = TypingCalculator.generateFeedback(65, 96.0, 3);
        assertNotNull(feedback);
        assertTrue(feedback.toLowerCase().contains("accuracy") || feedback.toLowerCase().contains("speed") || feedback.toLowerCase().contains("practice"));
    }

    @Test
    void shouldCompareCurrentAndPreviousPerformance() {
        assertTrue(TypingCalculator.comparePerformance(82, 70).contains("Improved"));
        assertTrue(TypingCalculator.comparePerformance(68, 75).contains("Dropped"));
        assertTrue(TypingCalculator.comparePerformance(80, 80).contains("consistent"));
    }

}
