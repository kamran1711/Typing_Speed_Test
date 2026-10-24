 package com.typing.component;

import com.typing.navigation.ViewType;
import com.typing.util.AnimationUtils;
import com.typing.util.IconUtils;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Reusable Sidebar Navigation Item.
 * Features vector icon, label, left active pill indicator, and smooth hover animation.
 */
public class SidebarNavButton extends HBox {

    private final ViewType viewType;
    private final Region activeIndicator;
    private final StackPane iconWrapper;
    private final Label textLabel;
    private boolean active = false;

    public SidebarNavButton(ViewType viewType, IconUtils.IconType iconType) {
        this.viewType = viewType;

        getStyleClass().add("nav-button");
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);
        setPrefHeight(38);
        setMaxWidth(Double.MAX_VALUE);

        activeIndicator = new Region();
        activeIndicator.setPrefWidth(0);
        activeIndicator.setVisible(false);

        iconWrapper = IconUtils.createIcon(iconType, 16, "#68716E");
        iconWrapper.getStyleClass().add("nav-icon");

        textLabel = new Label(viewType.getMenuTitle());
        textLabel.getStyleClass().add("nav-label");
        HBox.setHgrow(textLabel, Priority.ALWAYS);

        getChildren().addAll(iconWrapper, textLabel);

        // Hover & tactile feedback
        AnimationUtils.attachButtonFeedback(this);
    }

    public ViewType getViewType() {
        return viewType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            if (!getStyleClass().contains("active")) {
                getStyleClass().add("active");
            }
            activeIndicator.setVisible(true);
        } else {
            getStyleClass().remove("active");
            activeIndicator.setVisible(false);
        }
    }
}
