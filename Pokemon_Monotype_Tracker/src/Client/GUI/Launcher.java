package Client.GUI;

import Client.GUITest;
import Server.DatabaseAccessObjects.UserDAO;
import Server.LoginValidator;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

public class Launcher {
    private final Scene loginScreen;
    private UserDAO userDAO;

    public Launcher() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db");
            this.userDAO = new UserDAO(conn);
        } catch (SQLException e) {
            System.err.println("Could not connect to database");
            System.exit(1);
        }

        this.loginScreen = createLoginScreen();
    }

    private Scene createLoginScreen() {
        TextField usernameField = new TextField("Username");
        TextField passwordField = new TextField("Password");
        passwordField.setOnAction(Action -> {
            onLoginSubmitted(usernameField, passwordField);
        });
        VBox vbox = new VBox(usernameField, passwordField);
        Pane root = new Pane(vbox);
        return new Scene(root, 640, 480);
    }

    private void onLoginSubmitted(TextField usernameField, TextField passwordField) {
        try {
            if (!userDAO.userNameExists(usernameField.getText())) {
                return;
            }
            byte[] passwordHash = LoginValidator.getPasswordHash(passwordField.getText());
            byte[] storedHash = userDAO.getPasswordHash(usernameField.getText());
            if (Arrays.equals(passwordHash, storedHash)) {
                System.out.println("Login Success!");
            }
            else {
                System.out.println("Login Failed!");
            }
        }
        catch (SQLException ex) {
            System.err.println("Could not connect to database");
        }
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
        GUITest.main(args);
    }
}
