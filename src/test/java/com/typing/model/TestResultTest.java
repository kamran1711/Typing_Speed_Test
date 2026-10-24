package com.typing.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestResultTest {

    @Test
    @DisplayName("Verify TestResult computes correct grades based on WPM and Accuracy")
    void testGradeComputation() {
        // High score -> S Tier
        TestResult sResult = TestResult.create("1", 60, TestDifficulty.HARD, 95, 98.5, 1, 450);
        assertEquals("S", sResult.getGrade());
        assertTrue(sResult.getScore() > 0);

        // A+ Tier
        TestResult aPlusResult = TestResult.create("2", 60, TestDifficulty.MEDIUM, 80, 96.0, 2, 400);
        assertEquals("A+", aPlusResult.getGrade());

        // A Tier
        TestResult aResult = TestResult.create("3", 60, TestDifficulty.MEDIUM, 65, 93.0, 3, 300);
        assertEquals("A", aResult.getGrade());

        // B Tier
        TestResult bResult = TestResult.create("4", 30, TestDifficulty.EASY, 50, 90.0, 4, 200);
        assertEquals("B", bResult.getGrade());

        // C Tier
        TestResult cResult = TestResult.create("5", 60, TestDifficulty.HARD, 25, 75.0, 15, 150);
        assertEquals("C", cResult.getGrade());
    }

    @Test
    @DisplayName("Verify TestDifficulty display properties")
    void testTestDifficulty() {
        assertEquals("Easy", TestDifficulty.EASY.getDisplayName());
        assertEquals("Medium", TestDifficulty.MEDIUM.getDisplayName());
        assertEquals("Hard", TestDifficulty.HARD.getDisplayName());
    }

    @Test
    @DisplayName("Verify TestDuration seconds mapping")
    void testTestDuration() {
        assertEquals(30, TestDuration.THIRTY_SEC.getSeconds());
        assertEquals(60, TestDuration.SIXTY_SEC.getSeconds());
        assertEquals(120, TestDuration.TWO_MIN.getSeconds());
    }
}
