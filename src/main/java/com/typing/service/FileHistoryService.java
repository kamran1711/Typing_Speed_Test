
package com.typing.service;

import com.typing.model.TestResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileHistoryService implements IHistoryService {

    private final FileStorage storage;
    private final List<TestResult> results = new ArrayList<>();

    public FileHistoryService() {
        this(new FileStorage());
    }

    public FileHistoryService(FileStorage storage) {
        this.storage = storage;
        try {
            results.addAll(storage.loadAll());
            sortResults();
        } catch (IOException e) {
            throw new IllegalStateException("Could not load typing history.", e);
        }
    }

    private void sortResults() {
        results.sort(Comparator.comparing(
                TestResult::getTimestamp,
                Comparator.nullsLast(Comparator.reverseOrder())));
    }

    @Override
    public synchronized List<TestResult> getAllResults() {
        return new ArrayList<>(results);
    }

    @Override
    public synchronized List<TestResult> getRecentResults(int count) {
        int limit = Math.max(0, Math.min(count, results.size()));
        return new ArrayList<>(results.subList(0, limit));
    }

    @Override
    public synchronized void saveResult(TestResult result) {
        if (result == null) {
            return;
        }

        try {
            storage.save(result);
            results.add(result);
            sortResults();
        } catch (IOException e) {
            throw new IllegalStateException("Could not save typing result.", e);
        }
    }

    @Override
    public synchronized int getBestWpm() {
        return results.stream()
                .mapToInt(TestResult::getWpm)
                .max()
                .orElse(0);
    }

    @Override
    public synchronized double getAverageAccuracy() {
        return results.stream()
                .mapToDouble(TestResult::getAccuracy)
                .average()
                .orElse(0.0);
    }

    @Override
    public synchronized int getTotalTestsCount() {
        return results.size();
    }

    @Override
    public synchronized List<Integer> getRecentWpmTrend(int limit) {
        List<TestResult> chronological = new ArrayList<>(results);
        chronological.sort(Comparator.comparing(TestResult::getTimestamp));

        int start = Math.max(0, chronological.size() - Math.max(0, limit));
        List<Integer> trend = new ArrayList<>();

        for (int i = start; i < chronological.size(); i++) {
            trend.add(chronological.get(i).getWpm());
        }

        return trend;
    }

    @Override
    public synchronized void clearHistory() {
        try {
            storage.clear();
            results.clear();
        } catch (IOException e) {
            throw new IllegalStateException("Could not clear typing history.", e);
        }
    }
}
