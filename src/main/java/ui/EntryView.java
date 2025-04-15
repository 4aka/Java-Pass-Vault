package ui;

import be.sec.Entry;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.geometry.Insets;
import java.util.function.Consumer;

public class EntryView extends VBox {
    private final Entry entry;
    private final HBox collapsedBox;
    private final VBox expandedBox;
    private boolean isExpanded = false;
    private Consumer<Entry> onSelect;

    /**
     * Entry view
     * @param entry
     * @param onSelect
     */
    public EntryView(Entry entry, Consumer<Entry> onSelect) {
        this.entry = entry;
        this.onSelect = onSelect;
        this.setStyle("-fx-border-color: black; -fx-border-width: 3;");
        this.setSpacing(5);
        this.setPadding(new Insets(5));

        Label title = new Label("Entre: " + entry.getUrl());
        title.setFont(Font.font("System", 20));
        collapsedBox = new HBox(title);
        collapsedBox.setSpacing(10);
        collapsedBox.setPadding(new Insets(5));
        collapsedBox.setOnMouseClicked(e -> toggleExpand());

        expandedBox = new VBox();
        expandedBox.setSpacing(5);

        Label urlLabel = createCopyableLabel("URL: " + entry.getUrl());
        Label loginLabel = createCopyableLabel("Login: " + entry.getLogin());
        Label passLabel = createCopyableLabel("Password: " + entry.getPassword());
        // "*".repeat(entry.getPassword().length()));

        expandedBox.getChildren().addAll(loginLabel, passLabel, urlLabel);
        expandedBox.setVisible(false);

        this.getChildren().addAll(collapsedBox, expandedBox);
        this.setStyle("-fx-border-color: black; -fx-padding: 10; -fx-background-color: lightgray;");
        this.setOnMouseClicked(e -> onSelect.accept(entry));
    }

    /**
     * Copyable label
     * @param text
     * @return
     */
    private Label createCopyableLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("System", 16));
        Tooltip tooltip = new Tooltip("Click for copying");
        Tooltip.install(label, tooltip);

        label.setOnMouseClicked(e -> {
            String valueToCopy = text.substring(text.indexOf(":") + 2);
            copyToClipboard(valueToCopy);
            Tooltip feedback = new Tooltip("Copied to clipboard. Press Ctrl + V");
            Tooltip.install(label, feedback);
            feedback.show(label, e.getScreenX(), e.getScreenY());
            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ignored) {}
                javafx.application.Platform.runLater(feedback::hide);
            }).start();
        });

        return label;
    }

    /**
     * Copy data to clipboard
     * @param value
     */
    private void copyToClipboard(String value) {
        ClipboardContent content = new ClipboardContent();
        content.putString(value);
        Clipboard.getSystemClipboard().setContent(content);
    }

    /**
     * Visibility of entre
     */
    private void toggleExpand() {
        isExpanded = !isExpanded;
        expandedBox.setVisible(isExpanded);
    }
}
