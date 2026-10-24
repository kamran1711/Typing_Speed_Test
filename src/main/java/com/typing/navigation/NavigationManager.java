package com.typing.navigation;

import com.typing.controller.TypingTestController;
import com.typing.model.TestDifficulty;
import com.typing.model.TestDuration;
import com.typing.util.AnimationUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Coordinates screen navigation and view switching with smooth transitions.
 */
public class NavigationManager {

    private static final NavigationManager INSTANCE = new NavigationManager();

    private StackPane contentPane;
    private ViewType currentView;
    private final Map<ViewType, Node> viewCache = new HashMap<>();
    private final Map<ViewType, Object> controllerCache = new HashMap<>();

    private Consumer<String> titleChangeListener;
    private Consumer<ViewType> viewChangeListener;

    private NavigationManager() {
    }

    public static NavigationManager getInstance() {
        return INSTANCE;
    }

    public void init(StackPane contentPane, Consumer<String> titleChangeListener,
                     Consumer<ViewType> viewChangeListener) {
        this.contentPane = contentPane;
        this.titleChangeListener = titleChangeListener;
        this.viewChangeListener = viewChangeListener;
    }

    public void navigateTo(ViewType viewType) {
        if (contentPane == null || viewType == null) return;

        try {
            Node node;
            // Always reload or refresh TypingTest to ensure clean session state
            if (viewType == ViewType.TYPING_TEST || !viewCache.containsKey(viewType)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(viewType.getFxmlPath()));
                Parent loadedRoot = loader.load();
                Object controller = loader.getController();

                viewCache.put(viewType, loadedRoot);
                controllerCache.put(viewType, controller);
                node = loadedRoot;
            } else {
                node = viewCache.get(viewType);
            }

            contentPane.getChildren().clear();
            contentPane.getChildren().add(node);

            this.currentView = viewType;

            if (titleChangeListener != null) {
                titleChangeListener.accept(viewType.getPageTitle());
            }

            if (viewChangeListener != null) {
                viewChangeListener.accept(viewType);
            }

            // Trigger smooth fade & slide transition
            AnimationUtils.playPageTransition(node, null);

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to load view: " + viewType.getFxmlPath() + " - " + e.getMessage());
        }
    }

    /**
     * Navigates to the Typing Test screen and immediately initializes it with preset parameters.
     */
    public void startTestWith(TestDifficulty difficulty, TestDuration duration) {
        navigateTo(ViewType.TYPING_TEST);
        Object controller = controllerCache.get(ViewType.TYPING_TEST);
        if (controller instanceof TypingTestController testController) {
            testController.initializeWithSettings(difficulty, duration);
        }
    }

    public ViewType getCurrentView() {
        return currentView;
    }

    public Object getController(ViewType viewType) {
        return controllerCache.get(viewType);
    }
}
