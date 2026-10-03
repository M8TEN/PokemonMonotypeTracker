package Client.GUI.View;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SceneManager {
    private static Stage stage;
    private static SceneManager instance;

    public static SceneManager getInstance() {
        if (instance == null) {
            instance = new SceneManager();
        }
        return instance;
    }

    public static void createSceneManager(Stage stage) {
        SceneManager.stage = stage;
        instance = new SceneManager();
    }

    public static void swapScenes(Scene nextScene) {
        if (stage == null) {
            System.err.println("Stage is null");
            return;
        }
        stage.setScene(nextScene);
        stage.show();
    }

    public static Stage getLoadingStage(String message) {
        Stage loaadingStage = new Stage();
        loaadingStage.initModality(Modality.APPLICATION_MODAL);
        loaadingStage.initStyle(StageStyle.UNDECORATED);

        ProgressIndicator spinner = new ProgressIndicator();
        Label messageLabel = new Label(message);

        VBox sceneBox = new VBox(spinner, messageLabel);
        sceneBox.setAlignment(Pos.CENTER);
        sceneBox.setSpacing(10);
        loaadingStage.setScene(new Scene(sceneBox, 640, 480));

        return loaadingStage;
    }
}
