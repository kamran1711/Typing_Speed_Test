package com.typing.service;

import com.typing.service.mock.MockHistoryService;
import com.typing.service.mock.MockTypingEngineService;
import com.typing.service.mock.MockUserProfileService;

/**
 * =========================================================================
 * SERVICE LOCATOR & DEPENDENCY INJECTION REGISTRY
 * =========================================================================
 * Provides decoupled access to services for controllers.
 * Teammates can register their concrete implementations here:
 *
 * Example in teammate module:
 * ServiceLocator.getInstance().registerTypingEngine(new TeammateTypingEngine());
 * ServiceLocator.getInstance().registerHistoryService(new SqliteHistoryService());
 */
public class ServiceLocator {

    private static final ServiceLocator INSTANCE = new ServiceLocator();

    private ITypingEngineService typingEngineService;
    private IHistoryService historyService;
    private IUserProfileService userProfileService;

    private ServiceLocator() {
        // Defaults to robust, interactive mock implementations
        this.typingEngineService = new MockTypingEngineService();
        this.historyService = new MockHistoryService();
        this.userProfileService = new MockUserProfileService();
    }

    public static ServiceLocator getInstance() {
        return INSTANCE;
    }

    public ITypingEngineService getTypingEngine() {
        return typingEngineService;
    }

    public void registerTypingEngine(ITypingEngineService service) {
        if (service != null) {
            this.typingEngineService = service;
        }
    }

    public IHistoryService getHistoryService() {
        return historyService;
    }

    public void registerHistoryService(IHistoryService service) {
        if (service != null) {
            this.historyService = service;
        }
    }

    public IUserProfileService getUserProfileService() {
        return userProfileService;
    }

    public void registerUserProfileService(IUserProfileService service) {
        if (service != null) {
            this.userProfileService = service;
        }
    }
}
