package ui;

import be.sec.HashPass;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main extends Application {
    HashPass hashPass = new HashPass();
    MainScreen mainScreen = new MainScreen();
    Path passwordFile = Paths.get("password.hash"); // TODO where to save pass??
    VBox layout = new VBox();
    Label infoLabel = new Label();
    Button confirmButton = new Button("Enter");

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    /**
     *
     * @param stage
     */
    private void showLoginScreen(Stage stage) {
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        if (Files.notExists(passwordFile)) newUserScenario(stage); // Кейс 1: Немає пароля — створення
        else existsUserScenario(stage); // Кейс 2: Є пароль — вхід

        Scene scene = new Scene(layout, 300, 200);
        stage.setTitle("Pass Vault - Login");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Scenario with new user. When user does not have
     * general password and vault
     * @param stage
     */
    public void newUserScenario(Stage stage) {
        PasswordField newPass1 = new PasswordField();
        newPass1.setPromptText("Create new password");
        PasswordField newPass2 = new PasswordField();
        newPass2.setPromptText("Reenter password");

        layout.getChildren().addAll(newPass1, newPass2, confirmButton, infoLabel);

        confirmButton.setOnAction(e -> {
            String pass1 = newPass1.getText();
            String pass2 = newPass2.getText();

            if (!pass1.equals(pass2)) {
                infoLabel.setText("Passwords don't match!");
                return;
            }

            try {
                String hash = hashPass.hashPassword(pass1);
                Files.writeString(passwordFile, hash);
                mainScreen.start(new Stage());
                stage.close();
            } catch (IOException ex) {
                infoLabel.setText("Error while saving password");
            }
        });
    }

    /**
     * Scenario with exists user. When user login into
     * exists vault.
     * @param stage
     */
    public void existsUserScenario(Stage stage) {
        PasswordField loginPass = new PasswordField();
        loginPass.setPromptText("Enter password");

        layout.getChildren().addAll(loginPass, confirmButton, infoLabel);

        confirmButton.setOnAction(e -> {
            String input = loginPass.getText();

            try {
                String storedHash = Files.readString(passwordFile).trim();
                String inputHash = hashPass.hashPassword(input);
                if (storedHash.equals(inputHash)) {
                    mainScreen.start(new Stage());
                    stage.close();
                } else {
                    infoLabel.setText("Wrong password");
                }
            } catch (IOException ex) {
                infoLabel.setText("Error while reading password");
            }
        });
    }

    /**
     * Launch application
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
