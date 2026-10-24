
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

}
