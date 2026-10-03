package Client.GUI.View;

import Battle.Moves.Move;
import Client.GUI.Controller.DetailedPokemonInfoController;
import Client.GUI.Model.DetailedPokemonInfoModel;
import Shared.Pokemon;
import Shared.PokemonType;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.nio.file.Paths;

public class DetailedPokemonInfoScreen {
    private DetailedPokemonInfoModel model;
    private Button backButton;

    public DetailedPokemonInfoScreen(DetailedPokemonInfoModel model) {
        this.model = model;
    }

    public Scene constructScene() {
        Pokemon pokemon = model.getPokemon();
        ImageView spriteView = new ImageView(model.getFrontSprite());
        Label nameLabel = new Label(pokemon.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 25;");
        ImageView primaryTypeView = new ImageView(getTypeImage(pokemon.getPrimaryType()));
        VBox spriteColumn = new VBox(spriteView, nameLabel, primaryTypeView);
        spriteColumn.setSpacing(10);

        if (pokemon.getSecondaryType() != PokemonType.None) {
            ImageView secondaryTypeView = new ImageView(getTypeImage(pokemon.getSecondaryType()));
            spriteColumn.getChildren().add(secondaryTypeView);
        }

        GridPane statColumn = new GridPane();
        statColumn.setVgap(10);
        statColumn.setHgap(5);
        String[] statNames = {"HP", "Attack", "Defense", "Special", "Speed"};
        int total = 0;
        int[] baseStats = pokemon.getBaseStats();
        for (int i = 0; i < baseStats.length; i++) {
           statColumn.add(new Label(statNames[i] + ":"), 0, i);
           Label numberLabel = new Label(String.valueOf(baseStats[i]));
           GridPane.setHalignment(numberLabel, HPos.RIGHT);
           statColumn.add(numberLabel, 1, i);
            total += baseStats[i];
        }
        statColumn.add(new Label(("Total: ")), 0, baseStats.length);
        statColumn.add(new Label(String.valueOf(total)), 1, baseStats.length);

        GridPane movePane = new GridPane();
        movePane.setHgap(15);
        movePane.setVgap(5);
        int row = 0;
        for (Move move : model.getKnownMoves()) {
            if (row % 2 != 0) {
                Region background = new Region();
                background.setStyle("-fx-background-color: #E8E8E8;");
                GridPane.setFillWidth(background, true);
                GridPane.setFillHeight(background, true);
                movePane.add(background, 0, row, 3, 1);
            }
            Label moveNameLabel = new Label(move.getName());
            ImageView typeView = new ImageView(getTypeImage(PokemonType.values()[move.getType()]));
            Label ppLabel = new Label("" + move.getMaxPP());
            movePane.add(moveNameLabel, 0, row);
            movePane.add(typeView, 1, row);
            movePane.add(ppLabel, 2, row);
            row++;
        }

        ScrollPane scrollPane = new ScrollPane(movePane);
        scrollPane.setFitToWidth(true);
        HBox.setHgrow(scrollPane, Priority.ALWAYS);
        backButton = new Button("Back");
        backButton.setAlignment(Pos.BOTTOM_RIGHT);
        HBox root = new HBox(spriteColumn, statColumn, scrollPane, backButton);
        root.setSpacing(20);
        return new Scene(root, 640, 480);
    }

    private Image getTypeImage(PokemonType pokemonType) {
        String upperPath = "Pokemon_Monotype_Tracker/Assets/Types/";
        return new Image(Paths.get(upperPath + pokemonType + ".png").toUri().toString());
    }

    public void AddHandler(DetailedPokemonInfoController controller) {
        backButton.setOnAction(actionEvent -> {
            controller.onBackPressed();
        });
    }
}
