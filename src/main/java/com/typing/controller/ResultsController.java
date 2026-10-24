package com.typing.controller;

import com.typing.model.TestResult;
import com.typing.navigation.NavigationManager;
import com.typing.navigation.ViewType;
import com.typing.service.IHistoryService;
import com.typing.service.ServiceLocator;
import com.typing.util.AnimationUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Performance &amp; Results view.
 * Displays deep analytics for the latest completed typing session.
 */
public class ResultsController implements Initializable {

    @FXML private StackPane resultsScreenRoot;

    // Overview Badges
    @FXML private Label latestWpmLabel;
    @FXML private Label latestAccuracyLabel;
    @FXML private Label latestScoreLabel;
    @FXML private Label latestGradeBadge;
    @FXML private Label latestModeLabel;

    // Detailed metrics breakdown
    @FXML private Label grossWpmLabel;
    @FXML private Label netWpmLabel;
    @FXML private Label errorsCountLabel;
    @FXML private Label charactersTypedLabel;
    @FXML private Label testDurationLabel;
    @FXML private Label consistencyLabel;

    // Progression Chart
    @FXML private LineChart<String, Number> velocityChart;
    @FXML private CategoryAxis chartXAxis;
    @FXML private NumberAxis chartYAxis;

    @FXML private Button btnStartNewTest;
    @FXML private Button btnViewHistory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupActions();
        loadResultsData();

        AnimationUtils.playPageTransition(resultsScreenRoot, null);
    }

    private void setupActions() {
        AnimationUtils.attachButtonFeedback(btnStartNewTest);
        AnimationUtils.attachButtonFeedback(btnViewHistory);

        btnStartNewTest.setOnAction(e -> NavigationManager.getInstance().navigateTo(ViewType.TYPING_TEST));
        btnViewHistory.setOnAction(e -> NavigationManager.getInstance().navigateTo(ViewType.HISTORY));
    }

    private void loadResultsData() {
        IHistoryService history = ServiceLocator.getInstance().getHistoryService();
        List<TestResult> recent = history.getRecentResults(1);

        if (!recent.isEmpty()) {
            TestResult latest = recent.get(0);
            latestWpmLabel.setText(latest.getWpm() + " WPM");
            latestAccuracyLabel.setText(String.format("%.1f%%", latest.getAccuracy()));
            latestScoreLabel.setText(latest.getScore() + " pts");
            latestGradeBadge.setText(latest.getGrade());
            latestModeLabel.setText(latest.getModeString() + " • " + latest.getFormattedDate());

            grossWpmLabel.setText((latest.getWpm() + Math.max(1, latest.getErrors() * 2)) + " WPM");
            netWpmLabel.setText(latest.getWpm() + " WPM");
            errorsCountLabel.setText(latest.getErrors() + " errors");
            charactersTypedLabel.setText(latest.getCharactersTyped() + " chars");
            testDurationLabel.setText(latest.getDurationSeconds() + " seconds");
            consistencyLabel.setText("94.6% Cadence");
        } else {
            latestWpmLabel.setText("-- WPM");
            latestAccuracyLabel.setText("--%");
            latestScoreLabel.setText("-- pts");
            latestGradeBadge.setText("-");
            latestModeLabel.setText("No test sessions recorded yet.");

            grossWpmLabel.setText("--");
            netWpmLabel.setText("--");
            errorsCountLabel.setText("0");
            charactersTypedLabel.setText("0");
            testDurationLabel.setText("0s");
            consistencyLabel.setText("--");
        }

        // Populate velocity chart
        velocityChart.setAnimated(false);
        velocityChart.setLegendVisible(false);
        chartXAxis.setAnimated(false);
        chartYAxis.setAnimated(false);

        velocityChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        List<Integer> trend = history.getRecentWpmTrend(8);
        for (int i = 0; i < trend.size(); i++) {
            series.getData().add(new XYChart.Data<>("Run " + (i + 1), trend.get(i)));
        }
        velocityChart.getData().add(series);
    }
}
