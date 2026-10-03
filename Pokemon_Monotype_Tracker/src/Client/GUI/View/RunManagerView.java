package Client.GUI.View;

import Client.GUI.Controller.RunManagerController;
import Client.GUI.Model.RunManagerModel;
import RunManagement.Run;
import Shared.Pokemon;
import Shared.PokemonType;
import Shared.Team;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;

public class RunManagerView {
    private Button createRunButton;
    private final RunManagerController controller;
    private final RunManagerModel model;
    private Scene mainScene;
    private VBox runBox;
    private ComboBox<PokemonType> typeComboBox;

    public Scene getMainScene() {
        return mainScene;
    }

    public RunManagerView(RunManagerController controller, RunManagerModel model) {
        this.controller = controller;
        this.model = model;
        this.mainScene = constructScene();
        addHandler(controller);
        updateRuns();
    }

    public void updateRuns() {
        runBox.getChildren().clear();
        for (Run run : model.getAllRuns()) {
            runBox.getChildren().add(constructRunInfo(run));
        }
    }

    private Scene constructScene() {
        runBox = new VBox();
        createRunButton = new Button("Create Run");
        createRunButton.setAlignment(Pos.TOP_LEFT);
        typeComboBox = new ComboBox<>(FXCollections.observableArrayList(Arrays.copyOfRange(PokemonType.values(), 1, PokemonType.values().length)));
        typeComboBox.setValue(PokemonType.Normal);
        HBox topBar = new HBox(createRunButton, typeComboBox);
        topBar.setSpacing(20);
        VBox buttonBox = new VBox(topBar, runBox);
        buttonBox.setSpacing(20);
        return new Scene(buttonBox, 640, 480);
    }

    private HBox constructRunInfo(Run run) {
        ImageView typeView = new ImageView(SpriteLoader.getTypeIcon(run.getMonoType()));
        HBox runBox = new HBox(typeView);
        Team runTeam = run.getPlannedTeam();
        for (Pokemon pokemon : runTeam.getPokemonInParty()) {
            if (pokemon == null) {
                ImageView pokeball = new ImageView(SpriteLoader.getPokeball());
                runBox.getChildren().add(pokeball);
            }
            else {
                ImageView boxSprite = new ImageView(SpriteLoader.getPokemonBoxSprite(pokemon.getName()));
                runBox.getChildren().add(boxSprite);
            }
        }

        Button deleteButton = new Button("Delete Run");
        deleteButton.setAlignment(Pos.BASELINE_RIGHT);
        deleteButton.setOnAction(event -> {
            Platform.runLater(() -> {
                controller.onDeleteRunPressed(run, this::updateRuns);
                }
            );
        });
        Button editButton = new Button("Edit Run");
        editButton.setAlignment(Pos.BASELINE_RIGHT);
        editButton.setOnAction(event -> {
            controller.onEditRunPressed(run);
        });
        runBox.getChildren().addAll(editButton, deleteButton);
        runBox.setSpacing(15);
        return runBox;
    }

    public void addHandler(RunManagerController controller) {
        createRunButton.setOnAction(event -> {
            controller.onCreateRunPressed(typeComboBox.getValue());
        });
    }

}
