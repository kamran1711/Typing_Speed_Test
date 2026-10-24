package com.typing;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * =========================================================================
 * APPLICATION ENTRY POINT: JavaFX Desktop UI
 * Member 1 Module: Navigation, Styling, Dashboard Shell, &amp; UI Transitions
 * =========================================================================
 */
public class MainApp extends Application {

    private static final String APP_TITLE = "Typing Speed Test and Performance Analyzer";
    private static final double MIN_WIDTH = 1080;
    private static final double MIN_HEIGHT = 700;
    private static final double DEFAULT_WIDTH = 1200;
    private static final double DEFAULT_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/typing/fxml/main-layout.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            scene.setFill(Color.web("#F5F5F0"));

            // Attach core design system stylesheet
            String cssPath = Objects.requireNonNull(
                    getClass().getResource("/com/typing/css/app.css"),
                    "Could not find app.css"
            ).toExternalForm();
            scene.getStylesheets().add(cssPath);

            // Register scene with ThemeManager for dynamic Light / Dark switching
            com.typing.theme.ThemeManager.getInstance().registerScene(scene);

            // Global keyboard accelerator: Cmd+T (Mac) / Ctrl+T (Win/Linux) toggles theme instantly
            scene.getAccelerators().put(
                    new javafx.scene.input.KeyCodeCombination(
                            javafx.scene.input.KeyCode.T,
                            javafx.scene.input.KeyCombination.SHORTCUT_DOWN
                    ),
                    () -> com.typing.theme.ThemeManager.getInstance().toggleTheme()
            );

            primaryStage.setTitle(APP_TITLE);
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            primaryStage.setScene(scene);

            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fatal error starting Typing Speed Test application: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
