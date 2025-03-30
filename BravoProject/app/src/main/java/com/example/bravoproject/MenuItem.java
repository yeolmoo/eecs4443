package com.example.bravoproject;

public class MenuItem {
    private String menuName;
    private String activityClassName;
    // move to test activity

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
}