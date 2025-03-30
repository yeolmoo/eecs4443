package com.example.bravoproject;

import java.util.List;

public interface TestResultDAO {
    void insertTestResult(TestResult result);

    List<TestResult> getAllResults();

    List<TestResult> getResultsByParticipant(String participantId);

    void deleteAllResults();
}
