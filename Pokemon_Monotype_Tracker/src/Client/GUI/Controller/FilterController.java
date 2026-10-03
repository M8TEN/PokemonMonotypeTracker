package Client.GUI.Controller;

import Battle.Moves.Move;
import Client.Client;
import Client.GUI.Model.DetailedPokemonInfoModel;
import Client.GUI.Model.FilterScreenModel;
import Client.GUI.Model.RunManagerModel;
import Client.GUI.View.*;
import RunManagement.Run;
import Shared.Pokemon;
import Shared.PokemonType;
import Shared.Status;
import Shared.Team;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.nio.file.Paths;
import java.util.ArrayList;

public class FilterController {
    private final Client client;
    private FilterScreenModel model;
    private FilterView filterScreen;
    private boolean wasFull = false;

    public FilterController(Client client, FilterScreenModel model, FilterView filterScreen) {
        this.client = client;
        this.model = model;
        this.filterScreen = filterScreen;
        filterScreen.addHanlder(this);
        enableInitialRemoveButtons();
    }

    private void enableInitialRemoveButtons() {
        for (int i = 0; i < model.getFilteredPokemon().size(); i++) {
            Pokemon examinePokemon = model.getFilteredPokemon().get(i);
            if (isPokemonInRun(examinePokemon)) {
                filterScreen.getRemoveButtons()[i].setDisable(false);
            }
        }
    }

    private boolean isPokemonInRun(Pokemon pokemon) {
        for (Pokemon inTeamPokemon : model.getPokemonInTeam()) {
            if (inTeamPokemon == null) return false;
            if (pokemon.getPokedexID() ==  inTeamPokemon.getPokedexID()) {
                return true;
            }
        }
        return false;
    }

    public void getKnownMoves(int pokedexID) {
        Stage loadingStage = SceneManager.getLoadingStage("Fetching Known Moves");
        loadingStage.show();
        new Thread(() -> {
            Pokemon pokemon = client.getPokemon(pokedexID);
            ArrayList<Move> knownMoves = client.getKnownMoves(pokedexID);
            Platform.runLater(() -> {
                if (knownMoves.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Could not find any known moves");
                    alert.setContentText("Please try again later");
                    alert.showAndWait();
                    loadingStage.close();
                }
                else {
                    Image frontSprite = SpriteLoader.getPokemonFrontSprite(pokemon.getName());
                    DetailedPokemonInfoModel detailedModel = new DetailedPokemonInfoModel(frontSprite, pokemon, knownMoves);
                    DetailedPokemonInfoScreen detailedView = new DetailedPokemonInfoScreen(detailedModel);
                    Scene detailScene = detailedView.constructScene();
                    detailedView.AddHandler(new DetailedPokemonInfoController(filterScreen));
                    SceneManager.swapScenes(detailScene);
                    loadingStage.close();
                }
            });
        }).start();
    }

    public void onAddToTeamPressed(Pokemon pokemon, int removeButtonIndex) {
        int insertedInto = -1;
        for (int i = 0; i < model.getPokemonInTeam().length; ++i) {
            if (model.getPokemonInTeam()[i] == null) {
                model.getPokemonInTeam()[i] = pokemon;
                insertedInto = i;
                filterScreen.getRemoveButtons()[removeButtonIndex].setDisable(false);
                break;
            }
        }
        boolean wasFilled = insertedInto >= model.getPokemonInTeam().length - 1;
        if  (wasFilled) {
            for (Button addButton : filterScreen.getAddButtons()) {
                addButton.setDisable(wasFilled);
            }
        }
        wasFull = wasFilled;
    }

    public void onRemoveFromTeamPressed(Pokemon pokemon, int removeButtonIndex) {
        int pokemonWithSameID = 0;
        int i = 0;
        while (i < model.getPokemonInTeam().length) {
            Pokemon examinePokemon = model.getPokemonInTeam()[i];
            if (examinePokemon == null) break;
            if (examinePokemon.getPokedexID() == pokemon.getPokedexID()) {
                if (pokemonWithSameID == 0) {
                    shiftPokemonUp(i);
                }
                else i++;
                pokemonWithSameID++;
            }
            else i++;
        }
        filterScreen.getRemoveButtons()[removeButtonIndex].setDisable(pokemonWithSameID <= 1);
        if (wasFull) {
            for (Button addButton : filterScreen.getAddButtons()) {
                addButton.setDisable(false);
            }
            wasFull = false;
        }
    }

    private void shiftPokemonUp(int startIdx) {
        Pokemon[] teamPokemon = model.getPokemonInTeam();
        while (startIdx < model.getPokemonInTeam().length - 1) {
            teamPokemon[startIdx] = teamPokemon[startIdx + 1];
            ++startIdx;
        }
        teamPokemon[teamPokemon.length - 1] = null;
    }

    public void onSavePressed() {
        Team team = new Team(model.getPokemonInTeam());
        Stage loadingStage = SceneManager.getLoadingStage("Saving Run");
        loadingStage.show();

        new Thread(() -> {
            if (model.getRunID() == -1) {
                Status runAddStatus = client.addRun(model.getRunType(), team);
                Platform.runLater(() -> {
                    if (runAddStatus != Status.AddRunSuccess) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Run could not be added");
                        alert.setContentText("Please try again later");
                        alert.showAndWait();
                        SceneManager.swapScenes(filterScreen.getMainScene());
                        loadingStage.close();
                    }
                    else {
                        backToRunOverview(loadingStage);
                    }
                });
            }
            else {
                Run run = new Run(model.getRunType(), new Team(model.getPokemonInTeam()), model.getRunID());
                Status updateStatus = client.updateRun(run);
                Platform.runLater(() -> {
                    if (updateStatus != Status.UpdateRunSuccess) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Run could not be edited");
                        alert.setContentText("Please try again later");
                        alert.showAndWait();
                        loadingStage.close();
                    }
                    else {
                        backToRunOverview(loadingStage);
                    }
                });
            }
        }).start();
    }

    private void backToRunOverview(Stage loadingStage) {
        new Thread(() -> {
            ArrayList<Run> allRuns = client.requestRuns();
            Platform.runLater(() -> {
                RunManagerModel runModel = new RunManagerModel(allRuns);
                RunManagerController runController = new RunManagerController(client, runModel);
                RunManagerView runView = new RunManagerView(runController, runModel);
                SceneManager.swapScenes(runView.getMainScene());
                loadingStage.close();
            });
        }).start();
    }

}