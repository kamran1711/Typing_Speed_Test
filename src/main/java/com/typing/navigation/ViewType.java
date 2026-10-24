package com.typing.navigation;

public enum ViewType {
    DASHBOARD("Dashboard", "Dashboard Overview", "/com/typing/fxml/dashboard.fxml"),
    TYPING_TEST("Start Test", "Typing Speed Test", "/com/typing/fxml/typing-test.fxml"),
    RESULTS("Results", "Performance & Results", "/com/typing/fxml/results.fxml"),
    HISTORY("History", "Test History & Analytics", "/com/typing/fxml/history.fxml"),
    SETTINGS("Settings", "Application Settings", "/com/typing/fxml/settings.fxml");

    private final String menuTitle;
    private final String pageTitle;
    private final String fxmlPath;

    ViewType(String menuTitle, String pageTitle, String fxmlPath) {
        this.menuTitle = menuTitle;
        this.pageTitle = pageTitle;
        this.fxmlPath = fxmlPath;
    }

    public String getMenuTitle() {
        return menuTitle;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public String getFxmlPath() {
        return fxmlPath;
    }
}
