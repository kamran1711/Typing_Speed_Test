
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

class FileStorageTest {

    @TempDir
    Path tempDir;

    @Test
    void savesAndLoadsResult() throws IOException {
        FileStorage storage = new FileStorage(
                tempDir.resolve("history.csv"));

        TestResult result = new TestResult(
                "test-1", LocalDateTime.of(2026, 1, 1, 10, 0),
                60, TestDifficulty.MEDIUM, 75, 98.5,
                2, 400, 350, "A+");

        storage.save(result);

        List<TestResult> loaded = storage.loadAll();

        assertEquals(1, loaded.size());
        assertEquals("test-1", loaded.get(0).getId());
        assertEquals(75, loaded.get(0).getWpm());
        assertEquals(98.5, loaded.get(0).getAccuracy());
        assertEquals("A+", loaded.get(0).getGrade());
    }

    @Test
    void clearRemovesSavedHistory() throws IOException {
        FileStorage storage = new FileStorage(
                tempDir.resolve("history.csv"));

        storage.save(new TestResult(
                "test-2", LocalDateTime.now(), 30,
                TestDifficulty.EASY, 50, 95.0,
                3, 200, 200, "B"));

        storage.clear();

        assertTrue(storage.loadAll().isEmpty());
    }

    @Test
    void missingFileReturnsEmptyHistory() throws IOException {
        FileStorage storage = new FileStorage(
                tempDir.resolve("missing.csv"));

        assertTrue(storage.loadAll().isEmpty());
    }
}
