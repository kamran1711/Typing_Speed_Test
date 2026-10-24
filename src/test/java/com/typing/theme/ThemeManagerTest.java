package com.typing.theme;

import com.typing.navigation.ViewType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URL;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class ThemeManagerTest {

    @Test
    @DisplayName("Verify ThemeManager singleton and toggle behavior")
    void testThemeManagerToggle() {
        ThemeManager manager = ThemeManager.getInstance();
        assertNotNull(manager, "ThemeManager instance should not be null");

        // Force to LIGHT
        manager.setTheme(ThemeManager.ThemeMode.LIGHT);
        assertEquals(ThemeManager.ThemeMode.LIGHT, manager.getCurrentTheme());
        assertFalse(manager.isDarkMode());

        // Toggle to DARK
        manager.toggleTheme();
        assertEquals(ThemeManager.ThemeMode.DARK, manager.getCurrentTheme());
        assertTrue(manager.isDarkMode());

        // Toggle back to LIGHT
        manager.toggleTheme();
        assertEquals(ThemeManager.ThemeMode.LIGHT, manager.getCurrentTheme());
        assertFalse(manager.isDarkMode());
    }

    @Test
    @DisplayName("Verify ThemeManager listener callbacks")
    void testThemeManagerListener() {
        ThemeManager manager = ThemeManager.getInstance();
        manager.setTheme(ThemeManager.ThemeMode.LIGHT);

        AtomicReference<ThemeManager.ThemeMode> notifiedTheme = new AtomicReference<>();
        AtomicBoolean wasCalled = new AtomicBoolean(false);

        java.util.function.Consumer<ThemeManager.ThemeMode> listener = theme -> {
            notifiedTheme.set(theme);
            wasCalled.set(true);
        };

        manager.addThemeListener(listener);

        manager.setTheme(ThemeManager.ThemeMode.DARK);
        assertTrue(wasCalled.get(), "Listener should have been invoked");
        assertEquals(ThemeManager.ThemeMode.DARK, notifiedTheme.get());

        // Cleanup listener
        manager.removeThemeListener(listener);
        wasCalled.set(false);
        manager.setTheme(ThemeManager.ThemeMode.LIGHT);
        assertFalse(wasCalled.get(), "Removed listener should not be invoked");
    }

    @Test
    @DisplayName("Verify light and dark CSS stylesheets exist in classpath")
    void testStylesheetsExist() {
        URL lightCss = getClass().getResource("/com/typing/css/app.css");
        assertNotNull(lightCss, "app.css should exist in classpath");

        URL darkCss = getClass().getResource("/com/typing/css/dark-theme.css");
        assertNotNull(darkCss, "dark-theme.css should exist in classpath");
    }

    @Test
    @DisplayName("Verify all 5 FXML views exist in classpath")
    void testFxmlViewsExist() {
        for (ViewType view : ViewType.values()) {
            URL url = getClass().getResource(view.getFxmlPath());
            assertNotNull(url, "FXML view should exist: " + view.getFxmlPath());
        }

        URL mainLayout = getClass().getResource("/com/typing/fxml/main-layout.fxml");
        assertNotNull(mainLayout, "main-layout.fxml should exist");
    }
}
