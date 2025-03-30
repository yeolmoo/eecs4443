package com.example.bravoproject;

public class TestResult {
    private int id; // primary key
    private String participantId;
    private String condition; // sitting or walking
    private String menuType; // button type
    private long navigationTimeMs;
    private int misclicks;
    private boolean completed;
    private int batteryStart;
    private int batteryEnd;
    private int comfortScore; // e.g., 1–5
    private int fatigueScore; // e.g., 1–5
    private String timestamp;

    private String feedback;


    public TestResult(int id, String participantId, String condition, String menuType, long navigationTimeMs, int misclicks, boolean completed, int batteryStart, int batteryEnd, int comfortScore, int fatigueScore, String timestamp) {
        this.id = id;
        this.participantId = participantId;
        this.condition = condition;
        this.menuType = menuType;
        this.navigationTimeMs = navigationTimeMs;
        this.misclicks = misclicks;
        this.completed = completed;
        this.batteryStart = batteryStart;
        this.batteryEnd = batteryEnd;
        this.comfortScore = comfortScore;
        this.fatigueScore = fatigueScore;
        this.timestamp = timestamp;
        this.feedback = feedback;
    }

    public TestResult() {

    }

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

    public int getBatteryStart() {
        return batteryStart;
    }

    public void setBatteryStart(int batteryStart) {
        this.batteryStart = batteryStart;
    }

    public int getBatteryEnd() {
        return batteryEnd;
    }

    public void setBatteryEnd(int batteryEnd) {
        this.batteryEnd = batteryEnd;
    }

    public int getComfortScore() {
        return comfortScore;
    }

    public void setComfortScore(int comfortScore) {
        this.comfortScore = comfortScore;
    }

    public int getFatigueScore() {
        return fatigueScore;
    }

    public void setFatigueScore(int fatigueScore) {
        this.fatigueScore = fatigueScore;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFeedback() {
        return feedback;
    }
    public void setFeedback(String feedback) {
        this.feedback = feedback; }
}




