package Client.GUI.View;

import Client.GUI.Controller.StartHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import javax.swing.*;
import java.nio.file.Paths;

public class StartUpScreen {
    private Button loginButton;
    private Button registerButton;
    private Scene mainScene;

    public Scene getMainScene() {
        return mainScene;
    }

    public StartUpScreen(StartHandler startHandler) {
        mainScene = constructScene();
        addHandler(startHandler);
    }

    public Scene constructScene() {
        Label title = new Label("Pokemon Monotype Tracker");
        title.setStyle("-fx-font-size: 40px;");
        ImageView logo = new ImageView(Paths.get("Pokemon_Monotype_Tracker/Assets/Gufa.png").toUri().toString());
        logo.setFitHeight(150);
        logo.setFitWidth(150);
        this.loginButton = new Button("Login");
        this.registerButton = new Button("Register");
        loginButton.setPrefSize(150, 50);
        registerButton.setPrefSize(150, 50);
        VBox root = new VBox(title, logo, loginButton, registerButton);
        root.setAlignment(Pos.CENTER);
        VBox.setMargin(loginButton, new Insets(30, 0, 10, 0));
        return new Scene(root, 640, 480);
    }

    public void addHandler(StartHandler handler) {
        loginButton.setOnAction(event -> {
            handler.onLoginPressed();
        });
        registerButton.setOnAction(event -> {
            handler.onRegisterPressed();
        });
    }

}
