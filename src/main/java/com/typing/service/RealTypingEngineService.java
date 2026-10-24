
package com.typing.service;

import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.TestResult;
import com.typing.model.TypingMetrics;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.UUID;
import java.util.function.Consumer;

public class RealTypingEngineService implements ITypingEngineService {

    private static final String EASY_PASSAGE =
            "The quick brown fox jumps over the lazy dog. Practice typing every day to improve your speed.";

    private static final String MEDIUM_PASSAGE =
            "Software development requires careful planning, consistent practice, and attention to detail.";

    private static final String HARD_PASSAGE =
            "Efficient algorithms improve computational performance while maintaining accuracy and reliability.";

    private TestDifficulty currentDifficulty = TestDifficulty.MEDIUM;
    private TestDuration currentDuration = TestDuration.SIXTY_SEC;

    private String targetPassage = MEDIUM_PASSAGE;

    private boolean running = false;

    private int elapsedSeconds = 0;
    private int totalDurationSeconds = 60;

    private int currentWpm = 0;
    private double currentAccuracy = 100.0;
    private int currentErrors = 0;
    private int currentCharsTyped = 0;

    private Timeline timerTimeline;

    private Consumer<TypingMetrics> tickListener;
    private Consumer<TestResult> finishListener;


private void initTimer() {
    timerTimeline = new Timeline(
        new KeyFrame(Duration.seconds(1), event -> {
            if (!running) {
                return;
            }

            elapsedSeconds++;

            if (elapsedSeconds >= totalDurationSeconds) {
                elapsedSeconds = totalDurationSeconds;

                TestResult result = finishTest();

                if (finishListener != null) {
                    finishListener.accept(result);
                }

                return;
            }

            if (tickListener != null) {
                tickListener.accept(getCurrentMetrics());
            }
        })
    );

    timerTimeline.setCycleCount(Timeline.INDEFINITE);
}

@Override
public void startTest(TestDifficulty difficulty, TestDuration duration) {

    currentDifficulty = (difficulty != null)
            ? difficulty : TestDifficulty.MEDIUM;

    currentDuration = (duration != null)
            ? duration : TestDuration.SIXTY_SEC;

    totalDurationSeconds = currentDuration.getSeconds();

    elapsedSeconds = 0;
    currentWpm = 0;
    currentAccuracy = 100.0;
    currentErrors = 0;
    currentCharsTyped = 0;

    switch (currentDifficulty) {
        case EASY -> targetPassage = EASY_PASSAGE;
        case MEDIUM -> targetPassage = MEDIUM_PASSAGE;
        case HARD -> targetPassage = HARD_PASSAGE;
    }

    running = true;

    if (timerTimeline != null) {
        timerTimeline.stop();
    }

    initTimer();
    timerTimeline.playFromStart();
}


@Override
public TypingMetrics processInput(String currentInputText) {

    if (currentInputText == null) {
        currentInputText = "";
    }

    currentCharsTyped = currentInputText.length();

    currentErrors = TypingCalculator.countErrors(
            currentInputText, targetPassage
    );

    int correctCharacters = TypingCalculator.countCorrectCharacters(
            currentInputText, targetPassage
    );

    currentAccuracy = TypingCalculator.calculateAccuracy(
            correctCharacters, currentCharsTyped
    );

    currentWpm = TypingCalculator.calculateWpm(
            correctCharacters, elapsedSeconds
    );

    if (tickListener != null) {
        tickListener.accept(getCurrentMetrics());
    }

    return getCurrentMetrics();
}

@Override
public TestResult finishTest() {

    running = false;

    if (timerTimeline != null) {
        timerTimeline.stop();
    }

    return TestResult.create(
            UUID.randomUUID().toString().substring(0, 8),
            elapsedSeconds,
            currentDifficulty,
            currentWpm,
            currentAccuracy,
            currentErrors,
            currentCharsTyped
    );
}

@Override
public void reset() {

    running = false;

    if (timerTimeline != null) {
        timerTimeline.stop();
    }

    elapsedSeconds = 0;
    currentWpm = 0;
    currentAccuracy = 100.0;
    currentErrors = 0;
    currentCharsTyped = 0;
}

@Override
public String getTargetPassage() {
    return targetPassage;
}

@Override
public TypingMetrics getCurrentMetrics() {

    int remainingSeconds = Math.max(
            0,
            totalDurationSeconds - elapsedSeconds
    );

    double progress = targetPassage.isEmpty()
            ? 0.0
            : Math.min(
                    1.0,
                    currentCharsTyped / (double) targetPassage.length()
            );

    return new TypingMetrics(
            elapsedSeconds,
            remainingSeconds,
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
