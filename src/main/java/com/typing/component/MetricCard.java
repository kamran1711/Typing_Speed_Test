package com.typing.component;

import com.typing.util.AnimationUtils;
import com.typing.util.IconUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Reusable Metric/Statistic card component.
 * Displays metric title, large formatted number, status indicator, and icon badge.
 */
public class MetricCard extends VBox {

    private final Label titleLabel;
    private final Label valueLabel;
    private final Label subLabel;
    private final StackPane iconContainer;

    public MetricCard(String title, String initialValue, String subText,
                      IconUtils.IconType iconType, String iconColorHex) {
        getStyleClass().add("metric-card");
        setSpacing(10);
        setPadding(new Insets(20, 24, 20, 24));
        setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(this, Priority.ALWAYS);

        // Top row: Title and Icon badge
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);

        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("metric-title");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        iconContainer = IconUtils.createIcon(iconType, 16, iconColorHex != null ? iconColorHex : "#397568");
        StackPane iconBadge = new StackPane(iconContainer);
        iconBadge.getStyleClass().add("metric-icon-badge");

        topRow.getChildren().addAll(titleLabel, iconBadge);

        // Value
        valueLabel = new Label(initialValue);
        valueLabel.getStyleClass().add("metric-value");
        valueLabel.setMaxWidth(Double.MAX_VALUE);

        // Subtext / Trend indicator
        subLabel = new Label(subText);
        subLabel.getStyleClass().add("metric-subtext");
        subLabel.setMaxWidth(Double.MAX_VALUE);

        getChildren().addAll(topRow, valueLabel, subLabel);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
        AnimationUtils.pulseNode(valueLabel);
    }

    public void setSubText(String subText) {
        subLabel.setText(subText);
    }

    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
