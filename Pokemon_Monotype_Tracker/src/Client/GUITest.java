package Client;

import Server.DatabaseAccessObjects.PokemonDAO;
import Shared.Pokemon;
import Shared.PokemonType;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class GUITest extends Application {

    @Override
    public void start(Stage stage) {
        try {
            final Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db");
            final PokemonDAO pokemonDAO = new PokemonDAO(conn);
            VBox insideScroll = new VBox();
            ScrollPane scrollPane = new ScrollPane(insideScroll);
            TextField textField = new TextField("Enter Pokemon Type");
            textField.setOnAction(event -> {
                onTextSubmission(insideScroll, pokemonDAO, textField, event);
            });
            VBox vbox = new VBox(textField, scrollPane);

            Scene MainScene = new Scene(vbox, 640, 480);
            stage.setScene(MainScene);
            stage.setTitle("Pokemon Monotype Tracker");
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onTextSubmission(VBox vbox, PokemonDAO dao, TextField field, Event event) {
        try {
            PokemonType type = PokemonType.valueOf(field.getText().strip());
            ArrayList<Pokemon> filteredPokemon = dao.getPokemonByType(type);
            vbox.getChildren().clear();
            for (Pokemon pokemon : filteredPokemon) {
                Label name = new Label(pokemon.getName());
                Label typeA = new Label(pokemon.getPrimaryType().toString());
                Label typeB = new Label( pokemon.getSecondaryType() != PokemonType.None ? pokemon.getSecondaryType().toString() : "");
                name.setPrefWidth(105);
                typeA.setPrefWidth(105);
                typeB.setPrefWidth(105);
                HBox currentLine = new HBox(name, typeA, typeB);
                vbox.getChildren().add(currentLine);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Could not find Pokemon Type " + field.getText());
        } catch (SQLException e) {
            System.err.println("Could not connect to Database");
            e.printStackTrace();
        }
    }

    private static Scene constrctDetailedScene(Pokemon pokemon) {
        Label nameLabel = new Label(pokemon.getName());
        String[] statNames = {
          "HP",
          "Attack",
          "Defense",
          "Special",
          "Speed"
        };
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        int[] stats = pokemon.getBaseStats();
        for (int i = 0; i < statNames.length; i++) {
            Label statNameLabel = new Label(statNames[i]);
            Label actualStat = new Label(stats[i] + "");
            grid.add(statNameLabel, 0, i);
            grid.add(actualStat, 1, i);
        }
        Button button = new Button("Back");
        VBox root = new VBox(nameLabel, grid, button);
        return new Scene(root, 640, 480);
    }

    private static void swapScene(Stage stage, Scene nextScene) {
        stage.setScene(nextScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
