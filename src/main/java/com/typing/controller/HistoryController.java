package com.typing.controller;

import com.typing.component.ConfirmationDialog;
import com.typing.model.TestDifficulty;
import com.typing.model.TestResult;
import com.typing.navigation.NavigationManager;
import com.typing.navigation.ViewType;
import com.typing.service.IHistoryService;
import com.typing.service.ServiceLocator;
import com.typing.util.AnimationUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Test History &amp; Analytics view.
 */
public class HistoryController implements Initializable {

    @FXML private StackPane historyScreenRoot;
    @FXML private Label totalTestsLabel;
    @FXML private Label highestWpmLabel;
    @FXML private Label avgAccuracyLabel;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> filterCombo;
    @FXML private Button btnClearHistory;
    @FXML private Button btnStartNewTest;

    @FXML private TableView<TestResult> historyTable;
    @FXML private TableColumn<TestResult, String> colDate;
    @FXML private TableColumn<TestResult, String> colMode;
    @FXML private TableColumn<TestResult, String> colDuration;
    @FXML private TableColumn<TestResult, String> colWpm;
    @FXML private TableColumn<TestResult, String> colAccuracy;
    @FXML private TableColumn<TestResult, String> colErrors;
    @FXML private TableColumn<TestResult, String> colScore;
    @FXML private TableColumn<TestResult, String> colGrade;

    @FXML private VBox emptyStateBox;

    private FilteredList<TestResult> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTableColumns();
        setupFilters();
        setupActionButtons();
        refreshHistoryView();

        AnimationUtils.playPageTransition(historyScreenRoot, null);
    }

    private void setupTableColumns() {
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFormattedDate()));
        colMode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDifficultyString()));
        colDuration.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDurationString()));
        colWpm.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWpmString()));
        colAccuracy.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAccuracyString()));
        colErrors.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getErrors())));
        colScore.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getScoreString()));
        colGrade.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getGrade()));

        // Grade badge cell styling
        colGrade.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String grade, boolean empty) {
                super.updateItem(grade, empty);
                if (empty || grade == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Label badge = new Label(grade);
                    badge.getStyleClass().add("grade-badge");
                    if ("S".equals(grade) || "A+".equals(grade)) {
                        badge.getStyleClass().add("grade-s");
                    } else if ("A".equals(grade)) {
                        badge.getStyleClass().add("grade-a");
                    } else {
                        badge.getStyleClass().add("grade-b");
                    }
                    setGraphic(badge);
                    setText(null);
                }
            }
        });
    }

    private void setupFilters() {
        filterCombo.getItems().addAll("All Difficulties", "Easy", "Medium", "Hard");
        filterCombo.setValue("All Difficulties");
        filterCombo.setOnAction(e -> applyFilter());

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applyFilter());
    }

    private void setupActionButtons() {
        AnimationUtils.attachButtonFeedback(btnStartNewTest);
        AnimationUtils.attachButtonFeedback(btnClearHistory);

        btnStartNewTest.setOnAction(e -> NavigationManager.getInstance().navigateTo(ViewType.TYPING_TEST));

        btnClearHistory.setOnAction(e -> {
            ConfirmationDialog dialog = new ConfirmationDialog(
                    historyScreenRoot,
                    "Clear All Test History?",
                    "Are you sure you want to permanently delete all logged test results? This action cannot be undone.",
                    "Clear History",
                    "Cancel",
                    () -> {
                        ServiceLocator.getInstance().getHistoryService().clearHistory();
                        refreshHistoryView();
                    },
                    null
            );
            dialog.show();
        });
    }

    private void applyFilter() {
        if (filteredData == null) return;

        String selectedDiff = filterCombo.getValue();
        String query = searchField.getText() != null ? searchField.getText().trim().toLowerCase() : "";

        filteredData.setPredicate(result -> {
            // Difficulty filter
            if (selectedDiff != null && !selectedDiff.equals("All Difficulties")) {
                if (result.getDifficulty() == null ||
                        !result.getDifficulty().getDisplayName().equalsIgnoreCase(selectedDiff)) {
                    return false;
                }
            }
            // Search text filter (date, mode, wpm, grade)
            if (!query.isEmpty()) {
                boolean matches = result.getModeString().toLowerCase().contains(query)
                        || result.getFormattedDate().toLowerCase().contains(query)
                        || result.getGrade().toLowerCase().contains(query)
                        || String.valueOf(result.getWpm()).contains(query);
                if (!matches) return false;
            }
            return true;
        });

        updateEmptyState();
    }

    private void updateEmptyState() {
        boolean isEmpty = filteredData.isEmpty();
        historyTable.setVisible(!isEmpty);
        historyTable.setManaged(!isEmpty);
        emptyStateBox.setVisible(isEmpty);
        emptyStateBox.setManaged(isEmpty);
    }

    public void refreshHistoryView() {
        IHistoryService history = ServiceLocator.getInstance().getHistoryService();
        List<TestResult> all = history.getAllResults();

        totalTestsLabel.setText(all.size() + " Total Tests");
        highestWpmLabel.setText("Peak: " + history.getBestWpm() + " WPM");
        avgAccuracyLabel.setText(String.format("Average Accuracy: %.1f%%", history.getAverageAccuracy()));

        filteredData = new FilteredList<>(FXCollections.observableArrayList(all));
        historyTable.setItems(filteredData);
        applyFilter();
    }
}
