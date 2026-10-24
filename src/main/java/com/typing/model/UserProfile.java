package com.typing.model;

/**
 * User Profile representation for the dashboard header and user statistics.
 */
public class UserProfile {
    private String name;
    private String handle;
    private String initials;
    private int bestWpm;
    private double averageAccuracy;
    private int totalTests;
    private double totalHoursPracticed;

    public UserProfile() {
    }

    public UserProfile(String name, String handle, String initials, int bestWpm,
                       double averageAccuracy, int totalTests, double totalHoursPracticed) {
        this.name = name;
        this.handle = handle;
        this.initials = initials;
        this.bestWpm = bestWpm;
        this.averageAccuracy = averageAccuracy;
        this.totalTests = totalTests;
        this.totalHoursPracticed = totalHoursPracticed;
    }

    public static UserProfile createDefault() {
        return new UserProfile("Alex Mercer", "@alex_typing", "AM", 92, 98.4, 148, 6.2);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public int getBestWpm() {
        return bestWpm;
    }

    public void setBestWpm(int bestWpm) {
        this.bestWpm = bestWpm;
    }

    public double getAverageAccuracy() {
        return averageAccuracy;
    }

    public void setAverageAccuracy(double averageAccuracy) {
        this.averageAccuracy = averageAccuracy;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public void setTotalTests(int totalTests) {
        this.totalTests = totalTests;
    }

    public double getTotalHoursPracticed() {
        return totalHoursPracticed;
    }

    public void setTotalHoursPracticed(double totalHoursPracticed) {
        this.totalHoursPracticed = totalHoursPracticed;
    }
}
