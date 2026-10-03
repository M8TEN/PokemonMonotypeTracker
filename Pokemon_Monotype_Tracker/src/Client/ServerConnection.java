package Client;

import java.net.Socket;

public class ServerConnection {
    private final Socket connection;

    public Socket getConnection() {
        return connection;
    }

    public ServerConnection(Socket connection) {
        this.connection = connection;
    }
}
