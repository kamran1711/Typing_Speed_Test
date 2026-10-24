package com.typing.service;

import com.typing.service.mock.MockTypingEngineService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceLocatorTest {

    @Test
    @DisplayName("Verify ServiceLocator provides default mock implementations")
    void testDefaultServicesAvailable() {
        ServiceLocator locator = ServiceLocator.getInstance();
        assertNotNull(locator, "ServiceLocator singleton should not be null");

        assertNotNull(locator.getTypingEngine(), "Default typing engine service should be wired");
        assertNotNull(locator.getHistoryService(), "Default history service should be wired");
        assertNotNull(locator.getUserProfileService(), "Default user profile service should be wired");
    }

    @Test
    @DisplayName("Verify custom service registration for teammate modules")
    void testCustomServiceRegistration() {
        ServiceLocator locator = ServiceLocator.getInstance();
        ITypingEngineService currentEngine = locator.getTypingEngine();

        ITypingEngineService mockCustomEngine = new MockTypingEngineService();
        locator.registerTypingEngine(mockCustomEngine);

        assertSame(mockCustomEngine, locator.getTypingEngine(), "Registered custom service should be returned");

        // Restore original
        locator.registerTypingEngine(currentEngine);
    }
}
