package Client.GUI.Controller;

import Client.Client;
import Client.GUI.Model.RunManagerModel;
import Client.GUI.View.RunManagerView;
import Client.GUI.View.SceneManager;
import RunManagement.Run;
import Shared.Status;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.ArrayList;

public class LoginController {
    private final Client client;

    public LoginController(Client client) {
        this.client = client;
    }

    private void attemptLogin(String username, String password, Label error, Stage loadingStage) {
        new Thread(() -> {
            Status responseStatus = client.attemptLogin(username, password);
            if (responseStatus == Status.LoginSuccess) {
                ArrayList<Run> userRuns = client.requestRuns();
                Platform.runLater(() -> {
                    RunManagerModel runModel = new RunManagerModel(userRuns);
                    RunManagerController runController = new RunManagerController(client, runModel);
                    RunManagerView runView = new RunManagerView(runController, runModel);
                    SceneManager.swapScenes(runView.getMainScene());
                    loadingStage.close();
                });
            }
            else {
                Platform.runLater(() -> {
                    error.setText("Login Error: " + responseStatus);
                    error.setVisible(true);
                    loadingStage.close();
                });
            }
        }).start();
    }

    public void attemptLogin(String username, String password, Label error) {
        Stage loadingStage = SceneManager.getLoadingStage("Logging into user " + username);
        loadingStage.show();
        attemptLogin(username, password, error, loadingStage);
    }

    public void attemptRegister(String username, String password, Label error) {
        Stage loadingStage = SceneManager.getLoadingStage("Registering new user " + username);
        loadingStage.show();

        new Thread(() -> {
            Status registerStatus = client.registerNewUser(username, password);
            Platform.runLater(() -> {
                if (registerStatus == Status.RegisterSuccess) {
                    error.setVisible(false);
                    attemptLogin(username, password, error, loadingStage);
                } else {
                    error.setText("Register Error: " + registerStatus);
                    error.setVisible(true);
                    loadingStage.close();
                }
            });
        }).start();
    }
}
