package com.typing.util;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.SVGPath;

/**
 * Lucide-style vector icon provider.
 * Renders crisp, scalable SVG icons natively in JavaFX with CSS styling support.
 */
public class IconUtils {

    public enum IconType {
        DASHBOARD("M4 4h6v8H4zm10 0h6v5h-6zm0 9h6v7h-6zm-10 3h6v4H4z"),
        KEYBOARD("M2 5h20v14H2zm3 3h2v2H5zm4 0h2v2H9zm4 0h2v2h-2zm4 0h2v2h-2zM5 12h2v2H5zm4 0h6v2H9zm8 0h2v2h-2z"),
        PLAY("M6 4l15 8-15 8V4z"),
        TROPHY("M18 2H6v7a6 6 0 0 0 12 0V2zM4 5h2v4H4a2 2 0 0 1-2-2 2 2 0 0 1 2-2zm16 0h-2v4h2a2 2 0 0 0 2-2 2 2 0 0 0-2-2zM9 16h6v3H9zm-2 5h10v2H7z"),
        HISTORY("M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2zm0 18a8 8 0 1 1 8-8 8 8 0 0 1-8 8zm1-13h-2v6l5.25 3.15.75-1.23-4-2.37z"),
        SETTINGS("M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm7.4-2a7.6 7.6 0 0 0 0-2l2.1-1.6a.5.5 0 0 0 .1-.6l-2-3.5a.5.5 0 0 0-.6-.2l-2.5 1a7.8 7.8 0 0 0-1.7-1l-.4-2.6a.5.5 0 0 0-.5-.5h-4a.5.5 0 0 0-.5.5l-.4 2.6a7.8 7.8 0 0 0-1.7 1l-2.5-1a.5.5 0 0 0-.6.2l-2 3.5a.5.5 0 0 0 .1.6l2.1 1.6a7.6 7.6 0 0 0 0 2l-2.1 1.6a.5.5 0 0 0-.1.6l2 3.5a.5.5 0 0 0 .6.2l2.5-1a7.8 7.8 0 0 0 1.7 1l.4 2.6a.5.5 0 0 0 .5.5h4a.5.5 0 0 0 .5-.5l.4-2.6a7.8 7.8 0 0 0 1.7-1l2.5 1a.5.5 0 0 0 .6-.2l2-3.5a.5.5 0 0 0-.1-.6z"),
        SPEEDOMETER("M12 2a10 10 0 0 0-10 10c0 4.08 2.45 7.6 6 9.15v-2.3A8 8 0 0 1 4 12a8 8 0 1 1 16 0 8 8 0 0 1-4 6.85v2.3c3.55-1.55 6-5.07 6-9.15A10 10 0 0 0 12 2zm1 6h-2v5.17l3.7 2.22 1-1.66-2.7-1.62z"),
        TARGET("M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2zm0 18a8 8 0 1 1 8-8 8 8 0 0 1-8 8zm0-13a5 5 0 1 0 5 5 5 5 0 0 0-5-5zm0 8a3 3 0 1 1 3-3 3 3 0 0 1-3 3zm0-4a1 1 0 1 0 1 1 1 1 0 0 0-1-1z"),
        AWARD("M12 15a7 7 0 1 0-7-7 7 7 0 0 0 7 7zm0-12a5 5 0 1 1-5 5 5 5 0 0 1 5-5zm-3.8 14.2l-2.2 4.8 5-1.5 5 1.5-2.2-4.8a8.9 8.9 0 0 1-5.6 0z"),
        REFRESH("M1 4v6h6M23 20v-6h-6M20.49 9A9 9 0 0 0 5.64 5.64L1 10m22 4l-4.64 4.36A9 9 0 0 1 3.51 15"),
        CHECK("M20 6L9 17l-5-5"),
        ALERT("M12 8v4m0 4h.01M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2z"),
        ARROW_RIGHT("M5 12h14M12 5l7 7-7 7"),
        USER("M12 12a5 5 0 1 0-5-5 5 5 0 0 0 5 5zm0 2c-5.33 0-8 2.67-8 6v2h16v-2c0-3.33-2.67-6-8-6z"),
        FLAME("M12 2c.5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 1 1-14 0c0-1.15.43-2.3 1-3 1.5 2 3.5 2 4-1 1-6 2-8 2-8z"),
        CLOCK("M12 2a10 10 0 1 0 10 10A10 10 0 0 0 12 2zm1 14h-2V7h2z"),
        CHEVRON_RIGHT("M9 18l6-6-6-6"),
        BAR_CHART("M18 20V10M12 20V4M6 20v-6"),
        SUN("M12 7a5 5 0 1 0 0 10 5 5 0 0 0 0-10zm0-5a1 1 0 0 1 1 1v2a1 1 0 0 1-2 0V3a1 1 0 0 1 1-1zm0 18a1 1 0 0 1 1 1v2a1 1 0 0 1-2 0v-2a1 1 0 0 1 1-1zm10-8a1 1 0 0 1-1 1h-2a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1zM5 12a1 1 0 0 1-1 1H2a1 1 0 0 1 0-2h2a1 1 0 0 1 1 1zm14.07-7.07a1 1 0 0 1 0 1.41l-1.42 1.42a1 1 0 1 1-1.41-1.42l1.42-1.41a1 1 0 0 1 1.41 0zM6.34 17.66a1 1 0 0 1 0 1.41l-1.41 1.42a1 1 0 1 1-1.42-1.42l1.42-1.41a1 1 0 0 1 1.41 0zm12.73 1.41a1 1 0 0 1-1.41 0l-1.42-1.41a1 1 0 0 1 1.41-1.42l1.42 1.41a1 1 0 0 1 0 1.42zM6.34 6.34a1 1 0 0 1-1.41 0L3.51 4.93a1 1 0 1 1 1.42-1.42l1.41 1.42a1 1 0 0 1 0 1.41z"),
        MOON("M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z");

        private final String path;

        IconType(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

    public static SVGPath createSvg(IconType type, double targetSize, String fillHex) {
        SVGPath svg = new SVGPath();
        svg.setContent(type.getPath());
        svg.setFill(Color.web(fillHex != null ? fillHex : "#6C7475"));
        svg.setFillRule(FillRule.NON_ZERO);

        // Approximate scale based on standard 24x24 Lucide viewBox
        double scale = targetSize / 24.0;
        svg.setScaleX(scale);
        svg.setScaleY(scale);
        svg.getStyleClass().add("vector-icon");
        return svg;
    }

    public static StackPane createIcon(IconType type, double size, String colorHex) {
        StackPane container = new StackPane();
        container.setMinSize(size, size);
        container.setPrefSize(size, size);
        container.setMaxSize(size, size);

        SVGPath path = createSvg(type, size, colorHex);
        container.getChildren().add(path);
        container.getStyleClass().add("icon-container");
        return container;
    }

    public static StackPane createIcon(IconType type, double size) {
        return createIcon(type, size, "#6C7475");
    }
}
