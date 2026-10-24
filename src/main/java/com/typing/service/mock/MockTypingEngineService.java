package com.typing.service.mock;

import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.TestResult;
import com.typing.model.TypingMetrics;
import com.typing.service.ITypingEngineService;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * =========================================================================
 * [MEMBER 2 MOCK IMPLEMENTATION: REALISTIC TYPING SIMULATOR & CALCULATOR]
 * =========================================================================
 * Temporary mock implementation for UI preview, interactive testing, and demo.
 * Fully interactive: calculates real keystrokes, accuracy, errors, and countdown.
 * Member 2 can replace this by registering their production engine in ServiceLocator.
 */
public class MockTypingEngineService implements ITypingEngineService {

    private static final String PASSAGE_EASY =
            "The quick brown fox jumps over the lazy dog. Touch typing is a vital skill that enhances your programming speed and mental focus. Keep your fingers relaxed on the home row keys and maintain a steady cadence.";

    private static final String PASSAGE_MEDIUM =
            "Software engineering requires balancing architecture, testing, and algorithmic efficiency. In modern computing, intuitive user interfaces paired with resilient asynchronous pipelines deliver exceptional digital experiences; consistency always triumphs over chaotic bursts of effort.";

    private static final String PASSAGE_HARD =
            "Theoretical computer science examines deterministic finite automata, stochastic processes, and polynomial-time verifiability. For 2D transformations: T(v) = [cos(theta), -sin(theta); sin(theta), cos(theta)] * [x; y], preserving metric norms across non-Euclidean manifolds!";

    private TestDifficulty currentDifficulty = TestDifficulty.MEDIUM;
    private TestDuration currentDuration = TestDuration.SIXTY_SEC;
    private String targetPassage = PASSAGE_MEDIUM;

    private boolean running = false;
    private int elapsedSeconds = 0;
    private int totalDurationSeconds = 60;
    private Timeline timerTimeline;

    private int currentWpm = 0;
    private double currentAccuracy = 100.0;
    private int currentErrors = 0;
    private int currentCharsTyped = 0;

    private Consumer<TypingMetrics> tickListener;
    private Consumer<TestResult> finishListener;

    public MockTypingEngineService() {
        initTimer();
    }

    private void initTimer() {
        timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!running) return;
            elapsedSeconds++;
            int remaining = Math.max(0, totalDurationSeconds - elapsedSeconds);

            // Recompute WPM on each tick if characters typed
            recalculateMetrics("");

            if (tickListener != null) {
                tickListener.accept(getCurrentMetrics());
            }

            if (remaining <= 0) {
                TestResult result = finishTest();
                if (finishListener != null) {
                    finishListener.accept(result);
                }
            }
        }));
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    @Override
    public void startTest(TestDifficulty difficulty, TestDuration duration) {
        this.currentDifficulty = difficulty != null ? difficulty : TestDifficulty.MEDIUM;
        this.currentDuration = duration != null ? duration : TestDuration.SIXTY_SEC;
        this.totalDurationSeconds = this.currentDuration.getSeconds();
        this.elapsedSeconds = 0;

        switch (this.currentDifficulty) {
            case EASY -> this.targetPassage = PASSAGE_EASY;
            case HARD -> this.targetPassage = PASSAGE_HARD;
            case MEDIUM -> this.targetPassage = PASSAGE_MEDIUM;
        }

        this.currentCharsTyped = 0;
        this.currentErrors = 0;
        this.currentAccuracy = 100.0;
        this.currentWpm = 0;
        this.running = true;

        timerTimeline.playFromStart();
    }

    @Override
    public TypingMetrics processInput(String currentInputText) {
        if (currentInputText == null) currentInputText = "";
        this.currentCharsTyped = currentInputText.length();

        // Count errors against target passage
        int errors = 0;
        int checkLen = Math.min(currentCharsTyped, targetPassage.length());
        for (int i = 0; i < checkLen; i++) {
            if (currentInputText.charAt(i) != targetPassage.charAt(i)) {
                errors++;
            }
        }
        if (currentCharsTyped > targetPassage.length()) {
            errors += (currentCharsTyped - targetPassage.length());
        }
        this.currentErrors = errors;

        recalculateMetrics(currentInputText);

        // Check if finished entire passage before time
        if (currentCharsTyped >= targetPassage.length() && errors == 0) {
            TestResult result = finishTest();
            if (finishListener != null) {
                finishListener.accept(result);
            }
        }

        return getCurrentMetrics();
    }

    private void recalculateMetrics(String currentInputText) {
        if (currentCharsTyped > 0) {
            int correctChars = Math.max(0, currentCharsTyped - currentErrors);
            this.currentAccuracy = Math.max(0.0, Math.min(100.0, (correctChars / (double) currentCharsTyped) * 100.0));
        } else {
            this.currentAccuracy = 100.0;
        }

        double timeMinutes = Math.max(1, elapsedSeconds) / 60.0;
        // Standard formula: (all typed chars / 5.0) / minutes * accuracy factor
        double grossWords = currentCharsTyped / 5.0;
        double netWords = Math.max(0, grossWords - (currentErrors * 0.5));
        this.currentWpm = (int) Math.round(netWords / timeMinutes);
    }

    @Override
    public TestResult finishTest() {
        running = false;
        if (timerTimeline != null) {
            timerTimeline.stop();
        }

        TestResult result = TestResult.create(
                UUID.randomUUID().toString().substring(0, 8),
                totalDurationSeconds,
                currentDifficulty,
                Math.max(15, currentWpm), // reasonable floor for preview
                currentAccuracy,
                currentErrors,
                currentCharsTyped
        );
        return result;
    }

    @Override
    public void reset() {
        running = false;
        if (timerTimeline != null) {
            timerTimeline.stop();
        }
        elapsedSeconds = 0;
        currentCharsTyped = 0;
        currentErrors = 0;
        currentAccuracy = 100.0;
        currentWpm = 0;
    }

    @Override
    public String getTargetPassage() {
        return targetPassage;
    }

    @Override
    public TypingMetrics getCurrentMetrics() {
        int remaining = Math.max(0, totalDurationSeconds - elapsedSeconds);
        double progress = targetPassage.length() > 0
                ? Math.min(1.0, currentCharsTyped / (double) targetPassage.length())
                : 0.0;

        return new TypingMetrics(
                elapsedSeconds,
                remaining,
                currentWpm,
                currentAccuracy,
                currentErrors,
                currentCharsTyped,
                targetPassage.length(),
                progress
        );
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setOnTickListener(Consumer<TypingMetrics> listener) {
        this.tickListener = listener;
    }

    @Override
    public void setOnFinishListener(Consumer<TestResult> listener) {
        this.finishListener = listener;
    }
}
