package Client.GUI.View;

import Client.GUI.Controller.FilterController;
import Client.GUI.Model.FilterScreenModel;
import Shared.Pokemon;
import Shared.PokemonType;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.nio.file.Paths;
import java.util.ArrayList;

public class FilterView {
    private Scene mainScene;
    private VBox teamBox;
    private FilterScreenModel model;
    private GridPane filteredDetailBox;
    private FilterController filterController;
    private Button saveButton;
    private final Button[] addButtons;
    private final Button[] removeButtons;

    public Scene getMainScene() {
        return mainScene;
    }

    public Button[] getAddButtons() {
        return addButtons;
    }

    public Button[] getRemoveButtons() {
        return removeButtons;
    }

    public FilterView(FilterScreenModel model) {
        this.model = model;
        addButtons = new Button[model.getFilteredPokemon().size()];
        removeButtons = new Button[model.getFilteredPokemon().size()];
        mainScene = constructScene();
    }

    public Scene constructScene() {
        GridPane resultBox = new GridPane();
        resultBox.setHgap(20);
        resultBox.setVgap(15);
        ScrollPane resultScroll = new ScrollPane(resultBox);
        resultScroll.setFitToWidth(true);
        GridPane.setMargin(resultBox, new Insets(0, 10, 0, 10));
        HBox.setHgrow(resultScroll, Priority.ALWAYS);
        teamBox = new VBox();
        saveButton = new Button("Save");
        VBox rightColumn = new VBox(teamBox, saveButton);
        VBox.setVgrow(teamBox, Priority.ALWAYS);
        HBox upperContainer =  new HBox(resultScroll, rightColumn);
        updateTeamView();

        StackPane root =  new StackPane(upperContainer);
        mainScene = new Scene(root, 640, 480);
        filteredDetailBox = resultBox;
        return mainScene;
    }

    private void updateFilteredView(FilterController controller) {
        if (filteredDetailBox == null) return;
        ArrayList<Pokemon> filteredPokemon = model.getFilteredPokemon();
        filteredDetailBox.getChildren().clear();
        int row = 0;
        for (Pokemon pokemon : filteredPokemon) {
            ImageView boxSprite = new ImageView(SpriteLoader.getPokemonBoxSprite(pokemon.getName()));
            Label nameLabel = new Label(pokemon.getName());
            nameLabel.setOnMouseClicked(event -> {
                int pokedexID = pokemon.getPokedexID();
                controller.getKnownMoves(pokedexID);
            });
            ImageView primaryImageView = new ImageView(SpriteLoader.getTypeIcon(pokemon.getPrimaryType()));
            PokemonType secondaryType = pokemon.getSecondaryType();
            filteredDetailBox.add(boxSprite, 0, row);
            filteredDetailBox.add(nameLabel, 1, row);
            filteredDetailBox.add(primaryImageView, 2, row);
            if (secondaryType != PokemonType.None) {
                ImageView secondaryImageView = new ImageView(SpriteLoader.getTypeIcon(secondaryType));
                filteredDetailBox.add(secondaryImageView, 3, row);
            }
            Button addButton = new Button("Add");
            Button removeButton = new Button("Remove");
            addButtons[row] =  addButton;
            removeButtons[row] = removeButton;
            int temp = row;
            addButton.setOnAction(event -> {
                controller.onAddToTeamPressed(pokemon, temp);
                updateTeamView();
            });
            removeButton.setOnAction(event -> {
                controller.onRemoveFromTeamPressed(pokemon, temp);
                updateTeamView();
            });
            removeButton.setDisable(true);
            filteredDetailBox.add(addButton, 4, row);
            filteredDetailBox.add(removeButton, 5, row);
            row++;
        }
    }

    private void updateTeamView() {
        teamBox.getChildren().clear();
        for (Pokemon pokemon : model.getPokemonInTeam()) {
            if (pokemon == null) {
                teamBox.getChildren().add(new ImageView(SpriteLoader.getPokeball()));
            }
            else {
                teamBox.getChildren().add(new ImageView(SpriteLoader.getPokemonBoxSprite(pokemon.getName())));
            }
        }
    }

    public void addHanlder(FilterController controller) {
        this.filterController = controller;
        saveButton.setOnAction(event -> {
            controller.onSavePressed();
        });
        updateFilteredView(controller);
    }

}
