package com.typing.theme;

import javafx.scene.Scene;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * =========================================================================
 * THEME MANAGER: Dynamic Light / Dark Mode Controller
 * Coordinates theme switching across all views without reloading or state loss.
 * =========================================================================
 */
public class ThemeManager {

    public enum ThemeMode {
        LIGHT("Swiss Minimalist (Light)"),
        DARK("Obsidian Dark");

        private final String displayName;

        ThemeMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    private static final ThemeManager INSTANCE = new ThemeManager();

    private ThemeMode currentTheme = ThemeMode.LIGHT;
    private Scene registeredScene;
    private final List<Consumer<ThemeMode>> themeListeners = new CopyOnWriteArrayList<>();

    private static final String DARK_CSS_PATH = "/com/typing/css/dark-theme.css";

    private ThemeManager() {
    }

    public static ThemeManager getInstance() {
        return INSTANCE;
    }

    /**
     * Registers the primary application scene and applies the active theme.
     */
    public void registerScene(Scene scene) {
        this.registeredScene = scene;
        if (scene != null) {
            applyThemeToScene(scene, currentTheme);
        }
    }

    public ThemeMode getCurrentTheme() {
        return currentTheme;
    }

    public boolean isDarkMode() {
        return currentTheme == ThemeMode.DARK;
    }

    /**
     * Toggles between LIGHT and DARK mode smoothly.
     */
    public void toggleTheme() {
        setTheme(currentTheme == ThemeMode.LIGHT ? ThemeMode.DARK : ThemeMode.LIGHT);
    }

    /**
     * Sets the theme explicitly and updates all attached scenes and listeners.
     */
    public void setTheme(ThemeMode theme) {
        if (theme == null) return;
        this.currentTheme = theme;

        if (registeredScene != null) {
            applyThemeToScene(registeredScene, theme);
        }

        notifyListeners(theme);
    }

    private void applyThemeToScene(Scene scene, ThemeMode theme) {
        URL darkCssUrl = getClass().getResource(DARK_CSS_PATH);
        if (darkCssUrl == null) {
            System.err.println("Warning: Could not locate dark-theme.css at " + DARK_CSS_PATH);
            return;
        }

        String darkCss = darkCssUrl.toExternalForm();

        if (theme == ThemeMode.DARK) {
            if (!scene.getStylesheets().contains(darkCss)) {
                scene.getStylesheets().add(darkCss);
            }
            scene.setFill(Color.web("#0B1120"));
            if (scene.getRoot() != null && !scene.getRoot().getStyleClass().contains("theme-dark")) {
                scene.getRoot().getStyleClass().add("theme-dark");
            }
        } else {
            scene.getStylesheets().remove(darkCss);
            scene.setFill(Color.web("#F5F5F0"));
            if (scene.getRoot() != null) {
                scene.getRoot().getStyleClass().remove("theme-dark");
            }
        }
    }

    public void addThemeListener(Consumer<ThemeMode> listener) {
        if (listener != null && !themeListeners.contains(listener)) {
            themeListeners.add(listener);
        }
    }

    public void removeThemeListener(Consumer<ThemeMode> listener) {
        themeListeners.remove(listener);
    }

    private void notifyListeners(ThemeMode theme) {
        for (Consumer<ThemeMode> listener : themeListeners) {
            try {
                listener.accept(theme);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
