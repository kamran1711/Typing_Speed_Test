
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

    // ==================== EASY PASSAGES ====================
// Short sentences with familiar words.

private static final String[] EASY_PASSAGES = {
        "The sun is bright. Birds sing in the tree. I like to walk in the park every day.",

        "My dog runs in the yard. It likes to play with a ball. We have fun in the sun.",

        "I wake up early and drink a cup of milk. Then I read a book and get ready for school.",

        "The cat sits on the mat. It looks at the bird near the window and waits for it to move.",

        "My friend and I ride our bikes after school. We take a short path and stop near a lake.",

        "Every day I try to learn new words. I read them aloud and type them slowly until I get them right."
};


// ==================== MEDIUM PASSAGES ====================
// Longer sentences with varied vocabulary and sentence structures.

private static final String[] MEDIUM_PASSAGES = {
        "Learning a new skill requires patience, regular practice, and a willingness to improve through experience.",

        "Software development involves planning, writing code, testing features, and correcting errors before releasing an application.",

        "A healthy routine combines physical activity, balanced meals, sufficient sleep, and time for personal development.",

        "Successful teamwork depends on clear communication, mutual respect, shared responsibilities, and consistent cooperation.",

        "Technology allows people to exchange information quickly, collaborate across distances, and discover new opportunities for learning.",

        "Managing time effectively helps students complete assignments, prepare for examinations, and maintain a balanced daily schedule."
};


// ==================== HARD PASSAGES ====================
// Advanced vocabulary, longer words, and more complex sentences.

private static final String[] HARD_PASSAGES = {
        "Algorithmic optimization improves computational efficiency by reducing unnecessary operations while preserving the accuracy of the final result.",

        "Contemporary cybersecurity strategies require continuous monitoring, sophisticated authentication mechanisms, and comprehensive protection against emerging vulnerabilities.",

        "Interdisciplinary collaboration encourages researchers to combine specialized knowledge, evaluate competing hypotheses, and develop innovative solutions to complex problems.",

        "Reliable information systems depend on architectural scalability, systematic verification, fault tolerance, and carefully coordinated resource allocation.",

        "Environmental sustainability requires technological innovation, responsible consumption, long-term planning, and measurable improvements in resource utilization.",

        "Sophisticated computational models analyze extensive datasets, identify meaningful correlations, and generate predictions while accounting for uncertainty and potential limitations."
};

    private TestDifficulty currentDifficulty = TestDifficulty.MEDIUM;
    private TestDuration currentDuration = TestDuration.SIXTY_SEC;

    private String targetPassage = MEDIUM_PASSAGES[0];

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
    case EASY -> targetPassage = getRandomPassage(EASY_PASSAGES);
    case MEDIUM -> targetPassage = getRandomPassage(MEDIUM_PASSAGES);
    case HARD -> targetPassage = getRandomPassage(HARD_PASSAGES);
}

    running = true;

    if (timerTimeline != null) {
        timerTimeline.stop();
    }

    initTimer();
    timerTimeline.playFromStart();
}
private String getRandomPassage(String[] passages) {
    int randomIndex = (int) (Math.random() * passages.length);
    return passages[randomIndex];
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
