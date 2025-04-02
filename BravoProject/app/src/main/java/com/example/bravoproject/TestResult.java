package com.example.bravoproject;

public class TestResult {
    private int id; // primary key
    private String participantId;
    private String condition; // sitting or walking
    private String menuType; // button type
    private long navigationTimeMs;
    private int misclicks;
    private boolean completed;
    private int fatigueScore; // e.g., 1–5

    private String feedback;
    private long cpuUsage;
    private long memoryUsedKB;
    private String handedness;

    public String getHandedness() {
        return handedness;
    }

    public void setHandedness(String handedness) {
        this.handedness = handedness;
    }

    public TestResult(int id, String participantId, String condition, String menuType, long navigationTimeMs,
                      int misclicks, boolean completed, int fatigueScore,  String feedback,
                      long cpuUsage, long memoryUsedKB) {
        this.id = id;
        this.participantId = participantId;
        this.condition = condition;
        this.menuType = menuType;
        this.navigationTimeMs = navigationTimeMs;
        this.misclicks = misclicks;
        this.completed = completed;
        this.fatigueScore = fatigueScore;

        this.feedback = feedback;
        this.cpuUsage = cpuUsage;
        this.memoryUsedKB = memoryUsedKB;
    }

    public TestResult() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public long getNavigationTimeMs() {
        return navigationTimeMs;
    }

    public void setNavigationTimeMs(long navigationTimeMs) {
        this.navigationTimeMs = navigationTimeMs;
    }

    public int getMisclicks() {
        return misclicks;
    }

    public void setMisclicks(int misclicks) {
        this.misclicks = misclicks;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getFatigueScore() {
        return fatigueScore;
    }

    public void setFatigueScore(int fatigueScore) {
        this.fatigueScore = fatigueScore;
    }



    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public long getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(long cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public long getMemoryUsedKB() {
        return memoryUsedKB;
    }

    public void setMemoryUsedKB(long memoryUsedKB) {
        this.memoryUsedKB = memoryUsedKB;
    }
}
