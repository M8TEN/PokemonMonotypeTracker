package Client.GUI.Controller;

import Client.Client;
import Client.GUI.Model.FilterScreenModel;
import Client.GUI.Model.RunManagerModel;
import Client.GUI.View.FilterView;
import Client.GUI.View.SceneManager;
import RunManagement.Run;
import Shared.Pokemon;
import Shared.PokemonType;
import Shared.Status;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.ArrayList;

public class RunManagerController {
    private Client client;
    private RunManagerModel model;

    public RunManagerController(Client client, RunManagerModel model) {
        this.client = client;
        this.model = model;
    }

    public void onCreateRunPressed(PokemonType type) {
        Stage loadingStage = SceneManager.getLoadingStage("Fetching Pokemon with type " + type);
        loadingStage.show();

        new Thread(() -> {
            ArrayList<Pokemon> filteredPokemon = client.getPokemonOfType(type);
            Platform.runLater(() -> {
                if (filteredPokemon.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error fetching Pokemon with type " + type);
                    alert.setContentText("Please try again later");
                    alert.showAndWait();
                }
                else {
                    FilterScreenModel filterModel = new FilterScreenModel(type, filteredPokemon);
                    FilterView filterView = new FilterView(filterModel);
                    FilterController filterController = new FilterController(client, filterModel, filterView);
                    SceneManager.swapScenes(filterView.getMainScene());
                    loadingStage.close();
                }
            });
        }).start();
    }

    public void onEditRunPressed(Run run) {
        Stage loadingStage = SceneManager.getLoadingStage("Fetching Pokemon with type " + run.getMonoType());
        loadingStage.show();

        new Thread(() -> {
            ArrayList<Pokemon> filteredPokemon = client.getPokemonOfType(run.getMonoType());
            Platform.runLater(() -> {
                if (filteredPokemon.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Error fetching Pokemon with type " + run.getMonoType());
                    alert.setContentText("Please try again later");
                    alert.showAndWait();
                }
                else {
                    FilterScreenModel filterModel = new FilterScreenModel(run.getMonoType(), filteredPokemon, run.getPlannedTeam().getPokemonInParty(), run.getId());
                    FilterView filterView = new FilterView(filterModel);
                    FilterController filterController = new FilterController(client, filterModel, filterView);
                    SceneManager.swapScenes(filterView.getMainScene());
                    loadingStage.close();
                }
            });
        }).start();
    }

    public void onDeleteRunPressed(Run run, Runnable updateView) {
        Stage loadingStage = SceneManager.getLoadingStage("Fetching Pokemon with type " + run.getMonoType());
        loadingStage.show();

        new Thread(() -> {
            Status deleteStatus = client.deleteRun(run);
            if (deleteStatus == Status.DeleteRunSuccess) {
                model.getAllRuns().remove(run);
                Platform.runLater(() -> {
                    updateView.run();
                    loadingStage.close();
                });
            }
            else {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Run could not be deleted");
                    alert.setContentText("Please try again later");
                    alert.showAndWait();
                    loadingStage.close();
                });
            }
        }).start();
    }
}
