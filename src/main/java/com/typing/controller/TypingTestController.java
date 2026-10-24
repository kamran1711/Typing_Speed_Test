package com.typing.controller;

import com.typing.component.ConfirmationDialog;
import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.TestResult;
import com.typing.model.TypingMetrics;
import com.typing.navigation.NavigationManager;
import com.typing.navigation.ViewType;
import com.typing.service.ITypingEngineService;
import com.typing.service.ServiceLocator;
import com.typing.util.AnimationUtils;
import com.typing.util.IconUtils;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the dedicated Typing Test screen.
 * Handles HUD metrics (Countdown, WPM, Accuracy, Errors),
 * displays target passage, captures user keystrokes, and invokes ITypingEngineService.
 */
public class TypingTestController implements Initializable {

    @FXML private StackPane testScreenRoot;
    @FXML private Label modeBadgeLabel;
    @FXML private Label durationBadgeLabel;
    @FXML private ComboBox<TestDifficulty> difficultyCombo;
    @FXML private ComboBox<TestDuration> durationCombo;

    // Live HUD metrics
    @FXML private HBox statsHudContainer;
    @FXML private Label timerLabel;
    @FXML private ProgressBar timerProgressBar;
    @FXML private Label liveWpmLabel;
    @FXML private Label liveAccuracyLabel;
    @FXML private Label liveErrorsLabel;

    // Target text and Typing input
    @FXML private Label targetTextLabel;
    @FXML private TextArea typingInputArea;
    @FXML private Label typingHintLabel;

    // Controls
    @FXML private Button btnStartPause;
    @FXML private Button btnRestart;
    @FXML private Button btnFinish;

    private TestDifficulty activeDifficulty = TestDifficulty.MEDIUM;
    private TestDuration activeDuration = TestDuration.SIXTY_SEC;

    private boolean testActive = false;
    private boolean sessionFinished = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupPresetCombos();
        setupControlsFeedback();
        setupTypingListener();
        setupServiceListeners();

        // Initial reset state
        resetTestState();

        AnimationUtils.playStaggeredEntrance(List.of(
                statsHudContainer,
                targetTextLabel,
                typingInputArea
        ));
    }

    private void setupPresetCombos() {
        difficultyCombo.getItems().addAll(TestDifficulty.values());
        difficultyCombo.setValue(activeDifficulty);
        difficultyCombo.setOnAction(e -> {
            if (!testActive) {
                this.activeDifficulty = difficultyCombo.getValue();
                resetTestState();
            }
        });

        durationCombo.getItems().addAll(TestDuration.values());
        durationCombo.setValue(activeDuration);
        durationCombo.setOnAction(e -> {
            if (!testActive) {
                this.activeDuration = durationCombo.getValue();
                resetTestState();
            }
        });
    }

    private void setupControlsFeedback() {
        AnimationUtils.attachButtonFeedback(btnStartPause);
        AnimationUtils.attachButtonFeedback(btnRestart);
        AnimationUtils.attachButtonFeedback(btnFinish);

        btnStartPause.setOnAction(e -> toggleStartPause());
        btnRestart.setOnAction(e -> handleRestart());
        btnFinish.setOnAction(e -> handleManualFinish());
    }

    private void setupTypingListener() {
        typingInputArea.textProperty().addListener((obs, oldVal, newVal) -> {
            if (sessionFinished) return;

            // Auto-start session on first keystroke if not already active
            if (!testActive && newVal != null && !newVal.isEmpty()) {
                startTestSession();
            }

            if (testActive) {
                ITypingEngineService engine = ServiceLocator.getInstance().getTypingEngine();
                TypingMetrics metrics = engine.processInput(newVal);
                updateHud(metrics);
            }
        });
    }

    private void setupServiceListeners() {
        ITypingEngineService engine = ServiceLocator.getInstance().getTypingEngine();

        engine.setOnTickListener(metrics -> Platform.runLater(() -> updateHud(metrics)));

        engine.setOnFinishListener(result -> Platform.runLater(() -> onTestCompleted(result)));
    }

    public void initializeWithSettings(TestDifficulty difficulty, TestDuration duration) {
        if (difficulty != null) this.activeDifficulty = difficulty;
        if (duration != null) this.activeDuration = duration;

        difficultyCombo.setValue(this.activeDifficulty);
        durationCombo.setValue(this.activeDuration);

        resetTestState();
    }

    private void toggleStartPause() {
        if (sessionFinished) {
            resetTestState();
            startTestSession();
            return;
        }

        if (!testActive) {
            startTestSession();
        } else {
            // Pause
            testActive = false;
            btnStartPause.setText("Resume");
            typingInputArea.setDisable(true);
        }
    }

    private void startTestSession() {
        testActive = true;
        sessionFinished = false;
        btnStartPause.setText("Pause");
        btnStartPause.getStyleClass().remove("btn-primary");
        btnStartPause.getStyleClass().add("btn-secondary");

        typingInputArea.setDisable(false);
        typingInputArea.requestFocus();
        typingHintLabel.setText("Keep going! Maintain consistent rhythm and precision.");

        ITypingEngineService engine = ServiceLocator.getInstance().getTypingEngine();
        engine.startTest(activeDifficulty, activeDuration);
    }

    private void handleRestart() {
        if (testActive || !typingInputArea.getText().isEmpty()) {
            ConfirmationDialog dialog = new ConfirmationDialog(
                    testScreenRoot,
                    "Restart Test Session?",
                    "Are you sure you want to discard current test progress and restart with the current settings?",
                    "Restart",
                    "Keep Typing",
                    this::resetTestState,
                    null
            );
            dialog.show();
        } else {
            resetTestState();
        }
    }

    private void handleManualFinish() {
        if (testActive || !typingInputArea.getText().isEmpty()) {
            ITypingEngineService engine = ServiceLocator.getInstance().getTypingEngine();
            TestResult result = engine.finishTest();
            onTestCompleted(result);
        }
    }

    private void resetTestState() {
        testActive = false;
        sessionFinished = false;

        ITypingEngineService engine = ServiceLocator.getInstance().getTypingEngine();
        engine.reset();
        engine.startTest(activeDifficulty, activeDuration);
        engine.reset(); // prime passage without ticking timer

        targetTextLabel.setText(engine.getTargetPassage());
        typingInputArea.clear();
        typingInputArea.setDisable(false);

        modeBadgeLabel.setText(activeDifficulty.getDisplayName() + " Mode");
        durationBadgeLabel.setText(activeDuration.getLabel());

        btnStartPause.setText("Start Test");
        btnStartPause.getStyleClass().remove("btn-secondary");
        if (!btnStartPause.getStyleClass().contains("btn-primary")) {
            btnStartPause.getStyleClass().add("btn-primary");
        }

        timerLabel.setText(formatTime(activeDuration.getSeconds()));
        timerProgressBar.setProgress(1.0);
        liveWpmLabel.setText("0");
        liveAccuracyLabel.setText("100%");
        liveErrorsLabel.setText("0");
        typingHintLabel.setText("Click the text box and begin typing to start timer automatically.");
    }

    private void updateHud(TypingMetrics metrics) {
        if (metrics == null) return;

        timerLabel.setText(formatTime(metrics.getRemainingSeconds()));
        double progress = activeDuration.getSeconds() > 0
                ? (double) metrics.getRemainingSeconds() / activeDuration.getSeconds()
                : 0.0;
        timerProgressBar.setProgress(Math.max(0.0, Math.min(1.0, progress)));

        liveWpmLabel.setText(String.valueOf(metrics.getWpm()));
        liveAccuracyLabel.setText(String.format("%.0f%%", metrics.getAccuracy()));
        liveErrorsLabel.setText(String.valueOf(metrics.getErrorCount()));
    }

    private void onTestCompleted(TestResult result) {
        testActive = false;
        sessionFinished = true;
        typingInputArea.setDisable(true);
        btnStartPause.setText("Test Completed");
        btnStartPause.setDisable(true);

        // Save result via History Service
        ServiceLocator.getInstance().getHistoryService().saveResult(result);

        // Show comprehensive result modal
        showResultModal(result);
    }

    private void showResultModal(TestResult result) {
        ConfirmationDialog dialog = new ConfirmationDialog(
                testScreenRoot,
                "Test Complete! Performance Summary",
                "Great work! Here is your official typing speed benchmark result.",
                "Try Again",
                "View History",
                this::resetTestState,
                () -> NavigationManager.getInstance().navigateTo(ViewType.HISTORY)
        );

        // Create stylish result breakdown cards
        VBox resultSummaryBox = new VBox(14);
        resultSummaryBox.setAlignment(Pos.CENTER);
        resultSummaryBox.getStyleClass().add("result-modal-box");

        HBox gradeRow = new HBox(12);
        gradeRow.setAlignment(Pos.CENTER);
        Label gradeTitle = new Label("Performance Grade:");
        gradeTitle.getStyleClass().add("section-caption");
        Label gradeBadge = new Label(result.getGrade());
        gradeBadge.getStyleClass().addAll("grade-badge", "grade-large");
        gradeRow.getChildren().addAll(gradeTitle, gradeBadge);

        HBox statsRow = new HBox(16);
        statsRow.setAlignment(Pos.CENTER);

        VBox wpmCard = createMiniStatCard("SPEED", result.getWpm() + " WPM", true);
        VBox accCard = createMiniStatCard("ACCURACY", String.format("%.1f%%", result.getAccuracy()), false);
        VBox errCard = createMiniStatCard("ERRORS", String.valueOf(result.getErrors()), false);
        VBox ptsCard = createMiniStatCard("SCORE", result.getScore() + " pts", true);

        statsRow.getChildren().addAll(wpmCard, accCard, errCard, ptsCard);
        resultSummaryBox.getChildren().addAll(gradeRow, statsRow);

        dialog.setCustomContent(resultSummaryBox);
        dialog.show();
    }

    private VBox createMiniStatCard(String label, String value, boolean isAccent) {
        VBox box = new VBox(4);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("mini-stat-card");
        box.setPrefWidth(95);

        Label lbl = new Label(label);
        lbl.getStyleClass().add("section-caption");

        Label val = new Label(value);
        val.getStyleClass().addAll("metric-value", isAccent ? "text-accent" : "text-primary");
        val.setStyle("-fx-font-size: 15px; -fx-font-weight: 700;");

        box.getChildren().addAll(lbl, val);
        return box;
    }

    private String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
