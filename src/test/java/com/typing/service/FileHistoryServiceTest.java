
package com.typing.service;

import com.typing.model.TestDifficulty;
import com.typing.model.TestResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileHistoryServiceTest {

    @TempDir
    Path tempDir;

    private TestResult createResult(
            String id, int wpm, double accuracy) {
        return new TestResult(
                id,
                LocalDateTime.now(),
                60,
                TestDifficulty.MEDIUM,
                wpm,
                accuracy,
                2,
                300,
                200,
                "A"
        );
    }

    @Test
    void savesAndRetrievesResults() {
        FileStorage storage =
                new FileStorage(tempDir.resolve("history.csv"));
        FileHistoryService service = new FileHistoryService(storage);

        service.saveResult(createResult("test-1", 70, 95.0));
        service.saveResult(createResult("test-2", 90, 98.0));

        assertEquals(2, service.getAllResults().size());
        assertEquals(2, service.getTotalTestsCount());
        assertEquals(90, service.getBestWpm());
    }

    @Test
    void calculatesAverageAccuracy() {
        FileStorage storage =
                new FileStorage(tempDir.resolve("history.csv"));
        FileHistoryService service = new FileHistoryService(storage);

        service.saveResult(createResult("test-1", 70, 90.0));
        service.saveResult(createResult("test-2", 80, 100.0));

        assertEquals(95.0, service.getAverageAccuracy(), 0.001);
    }

    @Test
    void clearsResultsFromMemoryAndFile() throws IOException {
        FileStorage storage =
                new FileStorage(tempDir.resolve("history.csv"));
        FileHistoryService service = new FileHistoryService(storage);

        service.saveResult(createResult("test-1", 70, 95.0));
        service.clearHistory();

        assertTrue(service.getAllResults().isEmpty());
        assertEquals(0, service.getTotalTestsCount());
        assertEquals(0, service.getBestWpm());
        assertEquals(0.0, service.getAverageAccuracy(), 0.001);
        assertTrue(storage.loadAll().isEmpty());
    }

    @Test
    void loadsPreviouslySavedResults() {
        FileStorage storage =
                new FileStorage(tempDir.resolve("history.csv"));

        FileHistoryService firstService =
                new FileHistoryService(storage);
        firstService.saveResult(createResult("test-1", 75, 96.0));

        FileHistoryService secondService =
                new FileHistoryService(storage);

        List<TestResult> loaded = secondService.getAllResults();

        assertEquals(1, loaded.size());
        assertEquals("test-1", loaded.get(0).getId());
        assertEquals(75, loaded.get(0).getWpm());
    }
}
