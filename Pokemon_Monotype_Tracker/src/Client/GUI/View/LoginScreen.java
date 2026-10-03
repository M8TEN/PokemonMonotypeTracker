package Client.GUI.View;

import Client.GUI.Controller.LoginController;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {
    public static final int MODE_LOGIN = 0;
    public static final int MODE_REGISTER = 1;

    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;
    private int mode = MODE_LOGIN;

    public LoginScreen(int mode) {
        this.mode = mode;
    }

    public Scene constructScene() {
        this.usernameField = new TextField();
        this.passwordField = new PasswordField();
        Label usernameLabel = new Label("Username:");
        usernameField.setPromptText("Username");
        Label passwordLabel = new Label("Password:");
        passwordField.setPromptText("Password");
        this.errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");
        VBox vbox = new VBox(usernameLabel, usernameField, passwordLabel, passwordField, errorLabel);
        StackPane root = new StackPane();
        root.getChildren().add(vbox);
        return new Scene(root, 640, 480);
    }

    public void addHandler(LoginController controller) {
        if (mode == MODE_LOGIN) {
            passwordField.setOnAction(event -> {
                controller.attemptLogin(usernameField.getText(), passwordField.getText(), errorLabel);
            });
        }
        else if (mode == MODE_REGISTER) {
            passwordField.setOnAction(event -> {
                controller.attemptRegister(usernameField.getText(), passwordField.getText(), errorLabel);
            });
        }
    }
}
