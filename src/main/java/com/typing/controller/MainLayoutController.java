package com.typing.controller;

import com.typing.component.SidebarNavButton;
import com.typing.model.UserProfile;
import com.typing.navigation.NavigationManager;
import com.typing.navigation.ViewType;
import com.typing.service.ServiceLocator;
import com.typing.theme.ThemeManager;
import com.typing.util.AnimationUtils;
import com.typing.util.IconUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the outer application shell:
 * Left sidebar navigation, top header bar, and content switching container.
 */
public class MainLayoutController implements Initializable {

    @FXML private StackPane rootStackPane;
    @FXML private VBox sidebarContainer;
    @FXML private VBox navButtonsContainer;
    @FXML private HBox logoBox;
    @FXML private Label appTitleLabel;
    @FXML private Label appVersionLabel;

    @FXML private Label pageTitleLabel;
    @FXML private Label userWelcomeLabel;
    @FXML private Label userAvatarLabel;
    @FXML private Label userHandleLabel;

    @FXML private Button btnThemeToggle;
    @FXML private StackPane contentPane;

    private StackPane logoIcon;
    private final List<SidebarNavButton> navButtons = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupSidebar();
        setupUserProfile();
        setupThemeToggle();
        setupNavigation();
    }

    private void setupSidebar() {
        // App brand logo icon
        logoIcon = IconUtils.createIcon(IconUtils.IconType.KEYBOARD, 20, "#397568");
        logoBox.getChildren().add(0, logoIcon);

        // Sidebar Navigation Items
        createNavButton(ViewType.DASHBOARD, IconUtils.IconType.DASHBOARD);
        createNavButton(ViewType.TYPING_TEST, IconUtils.IconType.PLAY);
        createNavButton(ViewType.RESULTS, IconUtils.IconType.BAR_CHART);
        createNavButton(ViewType.HISTORY, IconUtils.IconType.HISTORY);
        createNavButton(ViewType.SETTINGS, IconUtils.IconType.SETTINGS);

        appVersionLabel.setText("Typing Studio • v1.0");
    }

    private void createNavButton(ViewType viewType, IconUtils.IconType iconType) {
        SidebarNavButton button = new SidebarNavButton(viewType, iconType);
        button.setOnMouseClicked(e -> NavigationManager.getInstance().navigateTo(viewType));
        navButtons.add(button);
        navButtonsContainer.getChildren().add(button);
    }

    private void setupUserProfile() {
        UserProfile user = ServiceLocator.getInstance().getUserProfileService().getCurrentUser();
        if (user != null) {
            userWelcomeLabel.setText("Welcome back, " + user.getName());
            userAvatarLabel.setText(user.getInitials());
            userHandleLabel.setText(user.getHandle());
        }
    }

    private void setupThemeToggle() {
        updateThemeToggleGraphic(ThemeManager.getInstance().getCurrentTheme());
        btnThemeToggle.setOnAction(e -> ThemeManager.getInstance().toggleTheme());
        AnimationUtils.attachButtonFeedback(btnThemeToggle);

        ThemeManager.getInstance().addThemeListener(this::updateThemeToggleGraphic);
    }

    private void updateThemeToggleGraphic(ThemeManager.ThemeMode theme) {
        boolean isDark = theme == ThemeManager.ThemeMode.DARK;
        IconUtils.IconType iconType = isDark ? IconUtils.IconType.SUN : IconUtils.IconType.MOON;
        String iconColor = isDark ? "#2DD4BF" : "#397568";
        StackPane icon = IconUtils.createIcon(iconType, 16, iconColor);
        btnThemeToggle.setGraphic(icon);

        Tooltip tooltip = new Tooltip(isDark ? "Switch to Light Mode (Swiss Minimalist)" : "Switch to Dark Mode (Obsidian Dark)");
        btnThemeToggle.setTooltip(tooltip);

        // Update brand logo accent color dynamically
        if (logoBox != null && !logoBox.getChildren().isEmpty()) {
            logoBox.getChildren().remove(0);
            logoIcon = IconUtils.createIcon(IconUtils.IconType.KEYBOARD, 20, isDark ? "#2DD4BF" : "#397568");
            logoBox.getChildren().add(0, logoIcon);
        }
    }

    private void setupNavigation() {
        NavigationManager.getInstance().init(
                contentPane,
                // Page title update listener
                title -> {
                    pageTitleLabel.setText(title);
                    AnimationUtils.playPageTransition(pageTitleLabel, null);
                },
                // Active nav button highlight listener
                activeView -> {
                    for (SidebarNavButton btn : navButtons) {
                        btn.setActive(btn.getViewType() == activeView);
                    }
                }
        );

        // Start by navigating to the Dashboard view
        NavigationManager.getInstance().navigateTo(ViewType.DASHBOARD);
    }

    public StackPane getRootStackPane() {
        return rootStackPane;
    }
}
