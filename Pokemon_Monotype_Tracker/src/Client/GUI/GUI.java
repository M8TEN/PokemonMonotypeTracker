package Client.GUI;

import Client.Client;
import Client.GUI.Controller.LoginController;
import Client.GUI.Controller.StartHandler;
import Client.GUI.View.LoginScreen;
import Client.GUI.View.SceneManager;
import Client.GUI.View.StartUpScreen;
import Server.DatabaseAccessObjects.PokemonDAO;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;

public class GUI extends Application {
    @Override
    public void start(Stage stage) {
        final int MAX_ATTEMPTS = 5;
        SceneManager.createSceneManager(stage);
        int attempts = 0;
        boolean connected = false;

        while (!connected && attempts < MAX_ATTEMPTS) {
            try {
                Socket socket = new Socket("127.0.0.1", 8080);
                Client client = new Client(socket);
                stage.setTitle("Pokemon Monotype Tracker");
                StartUpScreen startUp = new StartUpScreen(new StartHandler(client));
                stage.setScene(startUp.getMainScene());
                stage.getIcons().add(getStageIcon());
                stage.setOnCloseRequest(event -> {
                    client.disconnect();
                });
                stage.show();
                connected = true;

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.err.println("Could not connect to the server, trying again...");
                attempts++;
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (!connected) {
            System.err.println("Server timeout");
            System.exit(1);
        }
    }

    private Image getStageIcon() {
        Path imagePath = Paths.get("Pokemon_Monotype_Tracker/Assets/Gufa.png");
        return new Image(imagePath.toUri().toString());
    }
}
