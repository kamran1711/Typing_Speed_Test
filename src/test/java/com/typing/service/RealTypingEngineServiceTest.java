package com.typing.service;

import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.TypingMetrics;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class RealTypingEngineServiceTest {


@BeforeAll
static void initializeJavaFX() throws Exception {
    CountDownLatch startupLatch = new CountDownLatch(1);

    try {
        Platform.startup(startupLatch::countDown);
    } catch (IllegalStateException alreadyStarted) {
        startupLatch.countDown();
    }

    assertTrue(
            startupLatch.await(10, TimeUnit.SECONDS),
            "JavaFX platform did not start"
    );
}

private <T> T runOnJavaFXThread(java.util.concurrent.Callable<T> action)
        throws Exception {
    AtomicReference<T> result = new AtomicReference<>();
    AtomicReference<Throwable> error = new AtomicReference<>();
    CountDownLatch latch = new CountDownLatch(1);

    Platform.runLater(() -> {
        try {
            result.set(action.call());
        } catch (Throwable throwable) {
            error.set(throwable);
        } finally {
            latch.countDown();
        }
    });

    assertTrue(latch.await(10, TimeUnit.SECONDS), "JavaFX action timed out");

    if (error.get() != null) {
        throw new AssertionError("JavaFX action failed", error.get());
    }

    return result.get();
}

@Test
void startTestShouldInitializeCorrectly() throws Exception {
    runOnJavaFXThread(() -> {
        RealTypingEngineService engine = new RealTypingEngineService();

        engine.startTest(TestDifficulty.EASY, TestDuration.THIRTY_SEC);

        assertTrue(engine.isRunning());
        assertNotNull(engine.getTargetPassage());
        assertFalse(engine.getTargetPassage().isEmpty());
        assertEquals(30, engine.getCurrentMetrics().getRemainingSeconds());

        engine.reset();
        return null;
    });
}

@Test
void processInputShouldCalculateAccuracyAndErrors() throws Exception {
    runOnJavaFXThread(() -> {
        RealTypingEngineService engine = new RealTypingEngineService();

        engine.startTest(TestDifficulty.EASY, TestDuration.THIRTY_SEC);

        String target = engine.getTargetPassage();
        TypingMetrics metrics = engine.processInput(target.substring(0, 5));

        assertEquals(5, metrics.getCharactersTyped());
        assertEquals(100.0, metrics.getAccuracy(), 0.01);
        assertEquals(0, metrics.getErrorCount());

        engine.reset();
        return null;
    });
}

@Test
void incorrectCharactersShouldIncreaseErrorCount() throws Exception {
    runOnJavaFXThread(() -> {
        RealTypingEngineService engine = new RealTypingEngineService();

        engine.startTest(TestDifficulty.EASY, TestDuration.THIRTY_SEC);

        TypingMetrics metrics = engine.processInput("xxxxx");

        assertEquals(5, metrics.getCharactersTyped());
        assertTrue(metrics.getErrorCount() > 0);
        assertTrue(metrics.getAccuracy() < 100.0);

        engine.reset();
        return null;
    });
}


}
