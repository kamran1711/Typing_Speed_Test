package com.typing.controller;

import com.typing.component.ConfirmationDialog;
import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.model.UserProfile;
import com.typing.service.ServiceLocator;
import com.typing.util.AnimationUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Application Settings view.
 */
public class SettingsController implements Initializable {

    @FXML private StackPane settingsScreenRoot;
    @FXML private TextField userNameField;
    @FXML private TextField userHandleField;

    @FXML private ComboBox<TestDuration> defaultDurationCombo;
    @FXML private ComboBox<TestDifficulty> defaultDifficultyCombo;
    @FXML private ComboBox<com.typing.theme.ThemeManager.ThemeMode> themeModeCombo;
    @FXML private CheckBox soundEffectsCheck;
    @FXML private CheckBox instantTimerCheck;

    @FXML private Button btnSaveProfile;
    @FXML private Button btnResetData;
    @FXML private Label statusMessageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupFields();
        setupActions();
    }

    private void setupFields() {
        UserProfile user = ServiceLocator.getInstance().getUserProfileService().getCurrentUser();
        if (user != null) {
            userNameField.setText(user.getName());
            userHandleField.setText(user.getHandle());
        }

        defaultDurationCombo.getItems().addAll(TestDuration.values());
        defaultDurationCombo.setValue(TestDuration.SIXTY_SEC);

        defaultDifficultyCombo.getItems().addAll(TestDifficulty.values());
        defaultDifficultyCombo.setValue(TestDifficulty.MEDIUM);

        themeModeCombo.getItems().addAll(com.typing.theme.ThemeManager.ThemeMode.values());
        themeModeCombo.setValue(com.typing.theme.ThemeManager.getInstance().getCurrentTheme());
    }

    private void setupActions() {
        AnimationUtils.attachButtonFeedback(btnSaveProfile);
        AnimationUtils.attachButtonFeedback(btnResetData);

        themeModeCombo.setOnAction(e -> {
            com.typing.theme.ThemeManager.ThemeMode selected = themeModeCombo.getValue();
            if (selected != null && selected != com.typing.theme.ThemeManager.getInstance().getCurrentTheme()) {
                com.typing.theme.ThemeManager.getInstance().setTheme(selected);
                statusMessageLabel.setText("Theme changed to " + selected.getDisplayName());
                AnimationUtils.pulseNode(statusMessageLabel);
            }
        });

        // Listen for external theme switches (e.g., from top header toggle button)
        com.typing.theme.ThemeManager.getInstance().addThemeListener(theme -> {
            if (themeModeCombo.getValue() != theme) {
                themeModeCombo.setValue(theme);
            }
        });

        btnSaveProfile.setOnAction(e -> {
            UserProfile user = ServiceLocator.getInstance().getUserProfileService().getCurrentUser();
            if (user != null) {
                user.setName(userNameField.getText().trim());
                user.setHandle(userHandleField.getText().trim());
                String[] parts = user.getName().split(" ");
                String initials = "";
                for (String p : parts) {
                    if (!p.isEmpty()) initials += p.charAt(0);
                }
                user.setInitials(initials.toUpperCase());
                ServiceLocator.getInstance().getUserProfileService().updateProfile(user);
            }
            statusMessageLabel.setText("Preferences saved successfully!");
            AnimationUtils.pulseNode(statusMessageLabel);
        });

        btnResetData.setOnAction(e -> {
            ConfirmationDialog dialog = new ConfirmationDialog(
                    settingsScreenRoot,
                    "Reset All Application Data?",
                    "This will restore default test settings and reset your profile. Are you sure you wish to continue?",
                    "Reset",
                    "Cancel",
                    () -> {
                        ServiceLocator.getInstance().getHistoryService().clearHistory();
                        statusMessageLabel.setText("Application data reset to defaults.");
                    },
                    null
            );
            dialog.show();
        });
    }
}
