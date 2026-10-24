package com.typing.service.mock;

import com.typing.model.TestDifficulty;
import com.typing.model.TestResult;
import com.typing.service.IHistoryService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * =========================================================================
 * [MEMBER 3 MOCK IMPLEMENTATION: TEST HISTORY & DATA PERSISTENCE]
 * =========================================================================
 * Supplies realistic, sample test history for dashboard and history table preview.
 * When tests are completed, results are prepended to the live in-memory list.
 * Member 3 can replace this by registering their database/file DAO in ServiceLocator.
 */
public class MockHistoryService implements IHistoryService {

    private final List<TestResult> results = new ArrayList<>();

    public MockHistoryService() {
        populateInitialSampleData();
    }

    private void populateInitialSampleData() {
        // Believable recent test results
        results.add(new TestResult("res-001", LocalDateTime.now().minusHours(2), 60, TestDifficulty.MEDIUM,
                82, 98.2, 2, 410, 870, "A+"));
        results.add(new TestResult("res-002", LocalDateTime.now().minusHours(18), 30, TestDifficulty.EASY,
                91, 99.1, 1, 228, 935, "S"));
        results.add(new TestResult("res-003", LocalDateTime.now().minusDays(1), 60, TestDifficulty.HARD,
                74, 96.5, 4, 370, 780, "A"));
        results.add(new TestResult("res-004", LocalDateTime.now().minusDays(2), 120, TestDifficulty.MEDIUM,
                79, 97.8, 3, 790, 840, "A"));
        results.add(new TestResult("res-005", LocalDateTime.now().minusDays(3), 60, TestDifficulty.MEDIUM,
                85, 98.7, 1, 425, 905, "A+"));
        results.add(new TestResult("res-006", LocalDateTime.now().minusDays(4), 30, TestDifficulty.HARD,
                68, 94.2, 5, 170, 710, "B"));
        results.add(new TestResult("res-007", LocalDateTime.now().minusDays(5), 60, TestDifficulty.EASY,
                88, 99.4, 0, 440, 920, "S"));
        results.add(new TestResult("res-008", LocalDateTime.now().minusDays(6), 60, TestDifficulty.MEDIUM,
                76, 97.1, 3, 380, 805, "A"));
    }

    @Override
    public List<TestResult> getAllResults() {
        return Collections.unmodifiableList(new ArrayList<>(results));
    }

    @Override
    public List<TestResult> getRecentResults(int count) {
        int limit = Math.min(count, results.size());
        return Collections.unmodifiableList(new ArrayList<>(results.subList(0, limit)));
    }

    @Override
    public void saveResult(TestResult result) {
        if (result != null) {
            results.add(0, result);
        }
    }

    @Override
    public int getBestWpm() {
        int max = 0;
        for (TestResult r : results) {
            if (r.getWpm() > max) max = r.getWpm();
        }
        return max > 0 ? max : 92;
    }

    @Override
    public double getAverageAccuracy() {
        if (results.isEmpty()) return 98.4;
        double sum = 0;
        for (TestResult r : results) {
            sum += r.getAccuracy();
        }
        return Math.round((sum / results.size()) * 10.0) / 10.0;
    }

    @Override
    public int getTotalTestsCount() {
        return Math.max(148, results.size());
    }

    @Override
    public List<Integer> getRecentWpmTrend(int limit) {
        List<Integer> trend = new ArrayList<>();
        int count = Math.min(limit, results.size());
        // Chronological order (oldest to newest)
        for (int i = count - 1; i >= 0; i--) {
            trend.add(results.get(i).getWpm());
        }
        if (trend.isEmpty()) {
            trend.addAll(List.of(68, 74, 76, 79, 85, 88, 91, 82));
        }
        return trend;
    }

    @Override
    public void clearHistory() {
        results.clear();
    }
}
