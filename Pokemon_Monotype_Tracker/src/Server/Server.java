package Server;

import Server.DatabaseAccessObjects.MoveDAO;
import Server.DatabaseAccessObjects.PokemonDAO;
import Server.DatabaseAccessObjects.RunDAO;
import Server.DatabaseAccessObjects.UserDAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {
    private ArrayList<ClientHandler> activeConnections = new ArrayList<ClientHandler>();
    private final ServerSocket server;
    private Connection conn;
    private static final int PORT = 8080;
    private final PokemonDAO pokemonDAO;
    private final MoveDAO moveDAO;
    private final RunDAO runDAO;
    private final UserDAO userDAO;

    public ArrayList<ClientHandler> getActiveConnections() {
        return activeConnections;
    }

    public void setActiveConnections(ArrayList<ClientHandler> activeConnections) {
        this.activeConnections = activeConnections;
    }

    public ServerSocket getServer() {
        return server;
    }

    public Connection getConnection() {
        return conn;
    }

    public void setConnection(Connection connection) {
        this.conn = connection;
    }

    public Server(ServerSocket server, Connection connection, PokemonDAO pokemonDAO, MoveDAO moveDAO, RunDAO runDAO, UserDAO userDAO) {
        this.server = server;
        this.conn = connection;
        this.pokemonDAO = pokemonDAO;
        this.moveDAO = moveDAO;
        this.runDAO = runDAO;
        this.userDAO = userDAO;
    }

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT); Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db")) {
            System.out.println("Server Started");
            PokemonDAO pokemonDAO = new PokemonDAO(conn);
            Server server = new Server(serverSocket, conn, pokemonDAO, new MoveDAO(conn), new RunDAO(conn, pokemonDAO), new UserDAO(conn));
            server.awaitConnections();
        }
        catch (IOException e) {
            System.err.println("Could not establish server on port " + PORT);
            System.exit(1);
        }
        catch (SQLException e) {
            System.err.println("Error connecting to database");
            System.exit(1);
        }
    }

    private void awaitConnections() {
        while (true) {
            try {
                Socket client = this.server.accept();
                Thread clientThread = new Thread(new ClientHandler(client, pokemonDAO, moveDAO, runDAO, userDAO));
                clientThread.start();
            } catch (IOException e) {
                System.err.println("Accept failed");
            }
        }
    }
}
