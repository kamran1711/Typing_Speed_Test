package com.typing.service;

import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.TestResult;
import com.typing.model.TypingMetrics;

import java.util.function.Consumer;

/**
 * =========================================================================
 * [MEMBER 2 INTEGRATION INTERFACE: TYPING ENGINE & REAL-TIME CALCULATOR]
 * =========================================================================
 * Teammate responsible for Member 2 module:
 * Implement this interface with your actual typing calculation engine.
 * Swap your implementation in ServiceLocator or inject into controllers.
 */
public interface ITypingEngineService {

    /**
     * Prepares and starts a test session with the given difficulty and duration.
     */
    void startTest(TestDifficulty difficulty, TestDuration duration);

    /**
     * Evaluates the user's current typed text against the target passage.
     * Computes real-time WPM, accuracy %, error count, and progress.
     */
    TypingMetrics processInput(String currentInputText);

    /**
     * Ends the active test session and generates the official TestResult record.
     */
    TestResult finishTest();

    /**
     * Resets any active test session back to initial state.
     */
    void reset();

    /**
     * Retrieves the target text passage for the active session.
     */
    String getTargetPassage();

    /**
     * Returns the current live metrics snapshot.
     */
    TypingMetrics getCurrentMetrics();

    /**
     * Indicates whether a test session is currently active.
     */
    boolean isRunning();

    /**
     * Registers a callback for real-time timer ticks (e.g. once per second).
     */
    void setOnTickListener(Consumer<TypingMetrics> listener);

    /**
     * Registers a callback invoked when the countdown finishes automatically.
     */
    void setOnFinishListener(Consumer<TestResult> listener);
}
