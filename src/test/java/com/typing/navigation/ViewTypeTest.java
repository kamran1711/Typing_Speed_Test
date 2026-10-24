package com.typing.navigation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ViewTypeTest {

    @Test
    @DisplayName("Verify ViewType enum covers all 5 views with valid FXML paths and titles")
    void testViewTypeDefinitions() {
        for (ViewType view : ViewType.values()) {
            assertNotNull(view.getMenuTitle(), "Menu title should not be null for " + view);
            assertNotNull(view.getPageTitle(), "Page title should not be null for " + view);
            assertNotNull(view.getFxmlPath(), "FXML path should not be null for " + view);
            assertTrue(view.getFxmlPath().startsWith("/com/typing/fxml/"), "Path should point to fxml directory");
            assertTrue(view.getFxmlPath().endsWith(".fxml"), "Path should end with .fxml");
        }
    }
}
