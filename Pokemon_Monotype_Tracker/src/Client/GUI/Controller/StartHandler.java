package Client.GUI.Controller;

import Client.Client;
import Client.GUI.View.LoginScreen;
import Client.GUI.View.SceneManager;
import Server.DatabaseAccessObjects.PokemonDAO;
import javafx.application.Platform;
import javafx.stage.Stage;

public class StartHandler {
    private Client client;

    public StartHandler(Client client) {
        this.client = client;
    }

    public StartHandler(Client client, PokemonDAO pokemonDAO) {
        this.client = client;
    }

    public void onRegisterPressed() {
        switchToLoginScreen(LoginScreen.MODE_REGISTER);
    }

    public void onLoginPressed() {
        switchToLoginScreen(LoginScreen.MODE_LOGIN);
    }

    private void switchToLoginScreen(int mode) {
        LoginScreen loginScreen = new LoginScreen(mode);
        SceneManager.swapScenes(loginScreen.constructScene());
        loginScreen.addHandler(new LoginController(client));
    }
}
