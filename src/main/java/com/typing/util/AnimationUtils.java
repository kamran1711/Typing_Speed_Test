package com.typing.util;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.util.Duration;

import java.util.List;

/**
 * Utility helper for smooth, high-performance JavaFX UI transitions.
 * Follows design requirements: 150-250ms duration, natural easing, responsive feel.
 */
public class AnimationUtils {

    public static final Duration DEFAULT_DURATION = Duration.millis(180);
    public static final Duration FAST_DURATION = Duration.millis(150);
    public static final Duration MODAL_DURATION = Duration.millis(180);

    /**
     * Smooth page entrance: Fades in with slight horizontal translation (10px -> 0px).
     */
    public static void playPageTransition(Node node, Runnable onFinished) {
        if (node == null) return;

        node.setOpacity(0.0);
        node.setTranslateX(10.0);

        FadeTransition fade = new FadeTransition(DEFAULT_DURATION, node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.setInterpolator(Interpolator.EASE_OUT);

        TranslateTransition translate = new TranslateTransition(DEFAULT_DURATION, node);
        translate.setFromX(10.0);
        translate.setToX(0.0);
        translate.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition parallel = new ParallelTransition(fade, translate);
        if (onFinished != null) {
            parallel.setOnFinished(e -> onFinished.run());
        }
        parallel.play();
    }

    /**
     * Staggered entrance animation for summary cards.
     */
    public static void playStaggeredEntrance(List<? extends Node> nodes) {
        if (nodes == null || nodes.isEmpty()) return;

        int delay = 0;
        for (Node node : nodes) {
            if (node == null) continue;
            node.setOpacity(0.0);
            node.setTranslateY(8.0);

            FadeTransition fade = new FadeTransition(DEFAULT_DURATION, node);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.setInterpolator(Interpolator.EASE_OUT);

            TranslateTransition translate = new TranslateTransition(DEFAULT_DURATION, node);
            translate.setFromY(8.0);
            translate.setToY(0.0);
            translate.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition cardEntrance = new ParallelTransition(fade, translate);
            cardEntrance.setDelay(Duration.millis(delay));
            cardEntrance.play();

            delay += 45; // 45ms subtle stagger
        }
    }

    /**
     * Restrained tactile feedback for primary/secondary buttons.
     */
    public static void attachButtonFeedback(Node button) {
        if (button == null) return;

        button.setOnMousePressed(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(80), button);
            scale.setToX(0.985);
            scale.setToY(0.985);
            scale.play();
        });

        button.setOnMouseReleased(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(120), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }

    /**
     * Smooth modal dialog pop-in: Fades in background overlay and scales in dialog content.
     */
    public static void playDialogEntrance(Node overlay, Node dialogBox) {
        if (overlay != null) {
            overlay.setOpacity(0.0);
            FadeTransition fade = new FadeTransition(MODAL_DURATION, overlay);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        }

        if (dialogBox != null) {
            dialogBox.setScaleX(0.92);
            dialogBox.setScaleY(0.92);
            dialogBox.setOpacity(0.0);

            ScaleTransition scale = new ScaleTransition(MODAL_DURATION, dialogBox);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.setInterpolator(Interpolator.SPLINE(0.25, 0.1, 0.25, 1.0));

            FadeTransition fade = new FadeTransition(MODAL_DURATION, dialogBox);
            fade.setToValue(1.0);

            new ParallelTransition(scale, fade).play();
        }
    }

    /**
     * Smooth pulse highlight on metric value change.
     */
    public static void pulseNode(Node node) {
        if (node == null) return;
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(90), node);
        scaleUp.setToX(1.06);
        scaleUp.setToY(1.06);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(110), node);
        scaleDown.setToX(1.0);
        scaleDown.setToY(1.0);

        new SequentialTransition(scaleUp, scaleDown).play();
    }
}
