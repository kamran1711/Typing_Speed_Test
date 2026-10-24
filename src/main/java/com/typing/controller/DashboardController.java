package com.typing.controller;

import com.typing.component.MetricCard;
import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.TestResult;
import com.typing.model.UserProfile;
import com.typing.navigation.NavigationManager;
import com.typing.navigation.ViewType;
import com.typing.service.IHistoryService;
import com.typing.service.IUserProfileService;
import com.typing.service.ServiceLocator;
import com.typing.util.AnimationUtils;
import com.typing.util.IconUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Main Dashboard view.
 * Displays metric cards, Quick Start configuration, WPM trend chart, and recent test logs.
 */
public class DashboardController implements Initializable {

    @FXML private HBox summaryCardsContainer;

    // Quick Start Controls
    @FXML private VBox quickStartCard;
    @FXML private ToggleGroup durationGroup;
    @FXML private RadioButton radio30s;
    @FXML private RadioButton radio60s;
    @FXML private RadioButton radio120s;

    @FXML private ToggleGroup difficultyGroup;
    @FXML private RadioButton radioEasy;
    @FXML private RadioButton radioMedium;
    @FXML private RadioButton radioHard;

    @FXML private Button btnStartQuickTest;

    // Right Column Performance Summary & Chart
    @FXML private VBox performanceCard;
    @FXML private Label lastTestSummaryLabel;
    @FXML private Label lastTestWpmBadge;
    @FXML private Label lastTestAccuracyBadge;
    @FXML private LineChart<String, Number> trendChart;
    @FXML private CategoryAxis chartXAxis;
    @FXML private NumberAxis chartYAxis;

    // Recent History Table
    @FXML private VBox historyCard;
    @FXML private TableView<TestResult> recentHistoryTable;
    @FXML private TableColumn<TestResult, String> colDate;
    @FXML private TableColumn<TestResult, String> colMode;
    @FXML private TableColumn<TestResult, String> colDuration;
    @FXML private TableColumn<TestResult, String> colWpm;
    @FXML private TableColumn<TestResult, String> colAccuracy;
    @FXML private TableColumn<TestResult, String> colScore;
    @FXML private Button btnViewAllHistory;

    private MetricCard bestWpmCard;
    private MetricCard avgAccuracyCard;
    private MetricCard totalTestsCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSummaryCards();
        setupQuickStartControls();
        setupPerformanceTrendChart();
        setupRecentHistoryTable();
        refreshDashboardData();

        // Staggered entrance animation for dashboard cards
        AnimationUtils.playStaggeredEntrance(List.of(
                summaryCardsContainer,
                quickStartCard,
                performanceCard,
                historyCard
        ));
    }

    private void setupSummaryCards() {
        IHistoryService historyService = ServiceLocator.getInstance().getHistoryService();
        IUserProfileService userService = ServiceLocator.getInstance().getUserProfileService();
        UserProfile user = userService.getCurrentUser();

        int bestWpm = historyService.getBestWpm();
        double avgAcc = historyService.getAverageAccuracy();
        int totalTests = historyService.getTotalTestsCount();

        bestWpmCard = new MetricCard(
                "Best WPM",
                bestWpm + " WPM",
                "+4.8% vs last week",
                IconUtils.IconType.SPEEDOMETER,
                "#397568"
        );

        avgAccuracyCard = new MetricCard(
                "Average Accuracy",
                String.format("%.1f%%", avgAcc),
                "Top 10% precision tier",
                IconUtils.IconType.TARGET,
                "#397568"
        );

        totalTestsCard = new MetricCard(
                "Total Tests",
                totalTests + " Tests",
                user != null ? String.format("%.1f hrs practiced", user.getTotalHoursPracticed()) : "Consistent Practice",
                IconUtils.IconType.AWARD,
                "#397568"
        );

        summaryCardsContainer.getChildren().clear();
        for (MetricCard card : List.of(bestWpmCard, avgAccuracyCard, totalTestsCard)) {
            card.setMaxWidth(Double.MAX_VALUE);
            HBox.setHgrow(card, Priority.ALWAYS);
            summaryCardsContainer.getChildren().add(card);
        }
    }

    private void setupQuickStartControls() {
        AnimationUtils.attachButtonFeedback(btnStartQuickTest);

        btnStartQuickTest.setOnAction(e -> {
            TestDuration duration = TestDuration.SIXTY_SEC;
            if (radio30s.isSelected()) duration = TestDuration.THIRTY_SEC;
            else if (radio120s.isSelected()) duration = TestDuration.TWO_MIN;

            TestDifficulty difficulty = TestDifficulty.MEDIUM;
            if (radioEasy.isSelected()) difficulty = TestDifficulty.EASY;
            else if (radioHard.isSelected()) difficulty = TestDifficulty.HARD;

            NavigationManager.getInstance().startTestWith(difficulty, duration);
        });
    }

    private void setupPerformanceTrendChart() {
        trendChart.setAnimated(false);
        trendChart.setLegendVisible(false);
        chartXAxis.setAnimated(false);
        chartYAxis.setAnimated(false);

        loadChartData();
    }

    private void loadChartData() {
        trendChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<Integer> trend = ServiceLocator.getInstance().getHistoryService().getRecentWpmTrend(8);
        for (int i = 0; i < trend.size(); i++) {
            series.getData().add(new XYChart.Data<>("T-" + (i + 1), trend.get(i)));
        }
        trendChart.getData().add(series);
    }

    private void setupRecentHistoryTable() {
        colDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedDate()));
        colMode.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDifficultyString()));
        colDuration.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDurationString()));
        colWpm.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getWpmString()));
        colAccuracy.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAccuracyString()));
        colScore.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getScoreString()));

        // Clean subtle badge for Mode
        colMode.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String mode, boolean empty) {
                super.updateItem(mode, empty);
                if (empty || mode == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(mode);
                    if ("Easy".equalsIgnoreCase(mode)) {
                        badge.getStyleClass().add("badge-easy");
                    } else if ("Hard".equalsIgnoreCase(mode)) {
                        badge.getStyleClass().add("badge-hard");
                    } else {
                        badge.getStyleClass().add("badge-medium");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });

        btnViewAllHistory.setOnAction(e -> NavigationManager.getInstance().navigateTo(ViewType.HISTORY));
        AnimationUtils.attachButtonFeedback(btnViewAllHistory);

        loadRecentHistory();
    }

    private void loadRecentHistory() {
        List<TestResult> recent = ServiceLocator.getInstance().getHistoryService().getRecentResults(5);
        recentHistoryTable.setItems(FXCollections.observableArrayList(recent));

        if (!recent.isEmpty()) {
            TestResult latest = recent.get(0);
            lastTestSummaryLabel.setText(latest.getModeString() + " • " + latest.getFormattedDate());
            lastTestWpmBadge.setText(latest.getWpm() + " WPM");
            lastTestAccuracyBadge.setText(String.format("%.1f%% Acc", latest.getAccuracy()));
        } else {
            lastTestSummaryLabel.setText("No tests recorded yet.");
            lastTestWpmBadge.setText("-- WPM");
            lastTestAccuracyBadge.setText("--% Acc");
        }
    }

    public void refreshDashboardData() {
        IHistoryService history = ServiceLocator.getInstance().getHistoryService();
        bestWpmCard.setValue(history.getBestWpm() + " WPM");
        avgAccuracyCard.setValue(String.format("%.1f%%", history.getAverageAccuracy()));
        totalTestsCard.setValue(history.getTotalTestsCount() + " Tests");
        loadRecentHistory();
        loadChartData();
    }
}
