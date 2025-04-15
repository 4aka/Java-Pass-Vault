package ui;

import be.sec.Entry;
import be.sec.GenPass;
import be.sec.SecureStorage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainScreen extends Application {
    private VBox listBox = new VBox();
    private List<Entry> entries = new ArrayList<>();

    // TODO Master password. Think about it.
    private String masterPassword = "1234";
    private Entry selected = null;

    @Override
    public void start(Stage stage) {
        try {entries = SecureStorage.load(masterPassword);
             entries.sort(Comparator.comparing(Entry::getLogin));
        } catch (Exception e) {e.printStackTrace();}

        Button newButton = new Button("New");
        Button editButton = new Button("Edit");
        Button deleteButton = new Button("Delete");

        HBox controlBar = new HBox(10, newButton, editButton, deleteButton);
        controlBar.setPadding(new Insets(10));

        listBox.setPadding(new Insets(10));
        renderList();

        newButton.setOnAction(_ -> showEditor(null));
        editButton.setOnAction(_ -> {if (selected != null) showEditor(selected);});
        deleteButton.setOnAction(_ -> {
            if (selected != null) {
                entries.remove(selected);
                saveEntries();
                renderList();
            }
        });

        VBox root = new VBox(controlBar, listBox);
        stage.setScene(new Scene(root, 500, 600));
        stage.setTitle("Pass Vault");
        stage.show();
    }

    /**
     * Show list of entries
     */
    private void renderList() {
        listBox.getChildren().clear();
        entries.sort(Comparator.comparing(Entry::getLogin));
        for (Entry entry : entries) {
            EntryView view = new EntryView(entry, e -> selected = e);
            listBox.getChildren().add(view);
        }
    }

    /**
     * Show entries editor
     */
    private void showEditor(Entry entryToEdit) {
        TextField loginField = new TextField();
        TextField passwordField = new TextField();
        TextField urlField = new TextField();
        GenPass generatePassword = new GenPass();

        if (entryToEdit != null) {
            loginField.setText(entryToEdit.getLogin());
            passwordField.setText(entryToEdit.getPassword());
            urlField.setText(entryToEdit.getUrl());
        }

        Button generatePasswordButton = new Button("Generate pswrd");
        generatePasswordButton
                .setOnAction(_ -> passwordField.setText(generatePassword.GeneratePassword(20)));

        Button save = new Button("Save");
        save.setOnAction(_ -> {
            if (entryToEdit != null) {
                entryToEdit.setLogin(loginField.getText());
                entryToEdit.setPassword(passwordField.getText());
                entryToEdit.setUrl(urlField.getText());
            } else {
                entries.addFirst(new Entry(loginField.getText(), passwordField.getText(), urlField.getText()));
            }
            saveEntries();
            renderList();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(_ -> renderList());

        HBox buttons = new HBox(10, generatePasswordButton, save, cancel);
        VBox editor = new VBox(5,
                new Label("Login:"), loginField,
                new Label("Password:"), passwordField,
                new Label("URL:"), urlField,
                buttons);
        editor.setPadding(new Insets(10));

        listBox.getChildren().clear();
        listBox.getChildren().add(editor);
    }

    /**
     * Save entre
     */
    private void saveEntries() {
        try {SecureStorage.save(entries, masterPassword);}
        catch (Exception e) {e.printStackTrace();}
    }
}
