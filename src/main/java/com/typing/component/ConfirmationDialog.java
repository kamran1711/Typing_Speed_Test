package com.typing.component;

import com.typing.util.AnimationUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Reusable dark-mode modal dialog.
 * Renders on top of a root StackPane with smooth backdrop fade and dialog scale-in.
 */
public class ConfirmationDialog extends StackPane {

    private final StackPane rootPane;
    private final VBox dialogBox;
    private final Label titleLabel;
    private final Label messageLabel;
    private final VBox customContentBox;
    private final Button confirmButton;
    private final Button cancelButton;

    public ConfirmationDialog(StackPane rootPane, String title, String message,
                              String confirmText, String cancelText,
                              Runnable onConfirm, Runnable onCancel) {
        this.rootPane = rootPane;

        getStyleClass().add("modal-backdrop");
        setAlignment(Pos.CENTER);

        dialogBox = new VBox(16);
        dialogBox.getStyleClass().add("dialog-box");
        dialogBox.setMaxWidth(480);
        dialogBox.setPadding(new Insets(24));

        titleLabel = new Label(title);
        titleLabel.getStyleClass().add("dialog-title");

        messageLabel = new Label(message);
        messageLabel.getStyleClass().add("dialog-message");
        messageLabel.setWrapText(true);

        customContentBox = new VBox(12);

        HBox actionRow = new HBox(12);
        actionRow.setAlignment(Pos.CENTER_RIGHT);

        cancelButton = new Button(cancelText != null ? cancelText : "Cancel");
        cancelButton.getStyleClass().add("btn-secondary");
        cancelButton.setOnAction(e -> {
            close();
            if (onCancel != null) onCancel.run();
        });

        confirmButton = new Button(confirmText != null ? confirmText : "Confirm");
        confirmButton.getStyleClass().add("btn-primary");
        confirmButton.setOnAction(e -> {
            close();
            if (onConfirm != null) onConfirm.run();
        });

        AnimationUtils.attachButtonFeedback(cancelButton);
        AnimationUtils.attachButtonFeedback(confirmButton);

        if (cancelText != null && !cancelText.isEmpty()) {
            actionRow.getChildren().add(cancelButton);
        }
        actionRow.getChildren().add(confirmButton);

        dialogBox.getChildren().addAll(titleLabel, messageLabel, customContentBox, actionRow);
        getChildren().add(dialogBox);
    }

    public void setCustomContent(Node node) {
        customContentBox.getChildren().clear();
        if (node != null) {
            customContentBox.getChildren().add(node);
        }
    }

    public void show() {
        if (!rootPane.getChildren().contains(this)) {
            rootPane.getChildren().add(this);
            AnimationUtils.playDialogEntrance(this, dialogBox);
        }
    }

    public void close() {
        rootPane.getChildren().remove(this);
    }
}
