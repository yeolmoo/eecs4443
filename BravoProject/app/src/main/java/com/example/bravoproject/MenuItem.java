package com.example.bravoproject;

public class MenuItem {
    private String menuName;
    private String activityClassName;
    private boolean completedSitting;
    private boolean completedWalking;

    public MenuItem(String menuName, String activityClassName) {
        this.menuName = menuName;
        this.activityClassName = activityClassName;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getActivityClassName() {
        return activityClassName;
    }

    public boolean isCompletedSitting() {
        return completedSitting;
    }

    public void setCompletedSitting(boolean completedSitting) {
        this.completedSitting = completedSitting;
    }

    public boolean isCompletedWalking() {
        return completedWalking;
    }

    public void setCompletedWalking(boolean completedWalking) {
        this.completedWalking = completedWalking;
    }
}
