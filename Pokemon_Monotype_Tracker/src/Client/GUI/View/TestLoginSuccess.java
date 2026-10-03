package Client.GUI.View;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TestLoginSuccess {

    public Scene constructScene() {
        Label label = new Label("Login Success!");
        final int width = 640;
        final int height = 480;
        StackPane root = new StackPane(label);
        return new Scene(root, width, height);
    }
}