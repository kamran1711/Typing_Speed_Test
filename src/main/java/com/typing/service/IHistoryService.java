package com.typing.service;

import com.typing.model.TestResult;

import java.util.List;

/**
 * =========================================================================
 * [MEMBER 3 INTEGRATION INTERFACE: TEST HISTORY & DATA PERSISTENCE]
 * =========================================================================
 * Teammate responsible for Member 3 module:
 * Implement this interface with your database/file storage backend (SQLite, JSON, CSV, etc.).
 * Swap your implementation in ServiceLocator or inject into controllers.
 */
public interface IHistoryService {

    /**
     * Retrieves all saved test records in descending order of timestamp.
     */
    List<TestResult> getAllResults();

    /**
     * Retrieves the most recent N test results.
     */
    List<TestResult> getRecentResults(int count);

    /**
     * Persists a new test result.
     */
    void saveResult(TestResult result);

    /**
     * Calculates the highest WPM achieved across history.
     */
    int getBestWpm();

    /**
     * Calculates the overall average typing accuracy (percentage).
     */
    double getAverageAccuracy();

    /**
     * Total number of completed tests recorded.
     */
    int getTotalTestsCount();

    /**
     * Returns a chronological list of WPM values for the trend chart.
     */
    List<Integer> getRecentWpmTrend(int limit);

    /**
     * Clears all recorded history.
     */
    void clearHistory();
}
