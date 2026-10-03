package Server;

import Battle.Moves.Move;
import Client.UserInfo;
import RunManagement.Run;
import Server.DatabaseAccessObjects.MoveDAO;
import Server.DatabaseAccessObjects.PokemonDAO;
import Server.DatabaseAccessObjects.RunDAO;
import Server.DatabaseAccessObjects.UserDAO;
import Shared.*;
import Shared.DTOs.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private final Gson gson = new GsonBuilder().create();
    private int userID;
    private String activeUsername;
    private boolean loggedIn = false;
    private PokemonDAO pokemonDAO;
    private MoveDAO moveDAO;
    private RunDAO runDAO;
    private UserDAO userDAO;

    public Socket getSocket() {
        return socket;
    }

    public String getActiveUsername() {
        return activeUsername;
    }

    public void setActiveUsername(String activeUsername) {
        this.activeUsername = activeUsername;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ClientHandler(Socket socket, PokemonDAO pokemonDAO, MoveDAO moveDAO, RunDAO runDAO, UserDAO userDAO) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.pokemonDAO = pokemonDAO;
        this.moveDAO = moveDAO;
        this.runDAO = runDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {
            try {
                Commands command = Commands.values()[in.readInt()];
                System.out.println("Received Command: " + command);
                if (command != Commands.Login && !isLoggedIn() && command != Commands.Register) {
                    in.readUTF(); // Discard command
                    out.writeInt(Status.LoginRequired.ordinal());
                    out.flush();
                    continue;
                }
                switch (command) {
                    case Register: {
                        String jsonString =  in.readUTF();
                        LoginDTO loginDTO = gson.fromJson(jsonString, LoginDTO.class);
                        if (userDAO.userNameExists(loginDTO.username)) {
                            out.writeInt(Status.ErrorUsernameAlreadyExists.ordinal());
                        }
                        else {
                            UserInfo userInfo = new UserInfo(-1, loginDTO.username, loginDTO.passwordHash);
                            userDAO.createEntry(userInfo, -1);
                            out.writeInt(Status.RegisterSuccess.ordinal());
                        }
                        out.flush();
                        break;
                    }
                    case Login: {
                        if (isLoggedIn()) { // Client is already logged in but wants to log in again
                            in.readUTF(); // Clear Data Stream from JSON String and discard request
                            out.writeInt(Status.ErrorAlreadyLoggedIn.ordinal());
                            out.flush();
                        }
                        else onLoginRequest(in, out);
                        break;
                    }
                    case GetPokemonOfType: {
                        getPokemonOfType(in, out);
                        break;
                    }

                    case AddRun: {
                        Status addStatus = AddRun();
                        out.writeInt(addStatus.ordinal());
                        out.flush();
                        break;
                    }

                    case GetPokemonMoves: {
                        int pokedexID =  in.readInt();
                        Status status = getPokemonMoves(pokedexID);
                        if (status != Status.KnownMoveQuerySuccess) {
                            out.writeInt(Status.ErrorDatabaseAccess.ordinal());
                            out.flush();
                        }
                        break;
                    }

                    case GetPokemonByID: {
                        int pokedexID =  in.readInt();
                        Status status = getPokemonByID(pokedexID);
                        if  (status != Status.PokemonIDQuerySuccess) {
                            out.writeInt(status.ordinal());
                            out.flush();
                        }
                        break;
                    }

                    case GetRuns: {
                        Status runStatus = getRuns();
                        if (runStatus != Status.RunQuerySuccess) {
                            out.writeInt(runStatus.ordinal());
                        }
                        break;
                    }

                    case UpdateRun: {
                        String runDTOJSON = in.readUTF();
                        RunDTO runDTO = gson.fromJson(runDTOJSON, RunDTO.class);
                        Status updateStatus = updateRun(runDTO.allRuns);
                        out.writeInt(updateStatus.ordinal());
                        out.flush();
                        break;
                    }

                    case DeleteRun: {
                        String runDTOJSON = in.readUTF();
                        RunDTO runDTO = gson.fromJson(runDTOJSON, RunDTO.class);
                        Status deleteStatus = deleteRun(runDTO.allRuns);
                        out.writeInt(deleteStatus.ordinal());
                        out.flush();
                        break;
                    }

                    case Bye: {
                        getSocket().close();
                        System.out.println(getActiveUsername() + " Disconnected");
                        break;
                    }

                    default: {
                        out.writeInt(Status.UnknownStatus.ordinal());
                        out.flush();
                        break;
                    }
                }
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                break;
            }
        }
        System.out.println("Client Disconnected");
        try {
            in.close();
            out.close();
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private Status updateRun(ArrayList<Run> runs) {
        try {
            for (Run run : runs) {
                runDAO.update(run, run.getId());
            }
            return Status.UpdateRunSuccess;
        } catch (SQLException e) {
            return Status.ErrorUpdateRun;
        }
    }

    private Status deleteRun(ArrayList<Run> runs) {
        try {
            for (Run run : runs) {
                runDAO.delete(run.getId());
            }
            return Status.DeleteRunSuccess;
        }
        catch (SQLException e) {
            return Status.ErrorDeleteRun;
        }
    }

    private Status getRuns() {
        try {
            RunDTO runDTO = runDAO.getAllRuns(userID);
            out.writeInt(Status.RunQuerySuccess.ordinal());
            String dtoJSONString = gson.toJson(runDTO);
            out.writeUTF(dtoJSONString);
            out.flush();
            return Status.RunQuerySuccess;
        }
        catch (Exception e) {
            return Status.ErrorRunQuery;
        }
    }

    private Status getPokemonByID(int pokedexID) {
        try {
            Pokemon pokemon = pokemonDAO.get(pokedexID);
            PokemonDTO pokemonDTO = new PokemonDTO(pokemon);
            out.writeInt(Status.PokemonIDQuerySuccess.ordinal());
            out.writeUTF(gson.toJson(pokemonDTO));
            out.flush();
            return Status.PokemonIDQuerySuccess;

        } catch (SQLException e) {
            return Status.ErrorDatabaseAccess;
        }
        catch (IOException e) {
            return Status.ErrorNetwork;
        }
    }

    private void onLoginRequest(DataInputStream in, DataOutputStream out) throws IOException {
        String jsonString = in.readUTF();
        LoginDTO dto = gson.fromJson(jsonString, LoginDTO.class);
        try {
            if (!userDAO.userNameExists(dto.username)) {
                out.writeInt(Status.LoginUnknownUser.ordinal());
            } else {
                byte[] storedHash = userDAO.getPasswordHash(dto.username);
                if (Arrays.equals(storedHash, dto.passwordHash)) {
                    out.writeInt(Status.LoginSuccess.ordinal());
                    setActiveUsername(dto.username);
                    setUserID(userDAO.getUserID(dto.username));
                    setLoggedIn(true);
                    System.out.println(getActiveUsername() + " Logged in");
                } else {
                    out.writeInt(Status.LoginFailure.ordinal());
                    setLoggedIn(false);
                }
            }
        }
        catch (Exception e) {
            out.writeInt(Status.LoginFailure.ordinal());
            setLoggedIn(false);
        }
        finally {
            out.flush();
        }
    }

    private void getPokemonOfType(DataInputStream in, DataOutputStream out) throws IOException {
        try {
            PokemonType type =  PokemonType.values()[in.readInt()];
            ArrayList<Pokemon> pokemonOfType = pokemonDAO.getPokemonByType(type);
            PokemonTypeListDTO dto = new PokemonTypeListDTO(type, pokemonOfType);
            String jsonString = gson.toJson(dto);
            out.writeInt(Status.TypeQuerySuccess.ordinal());
            out.writeUTF(jsonString);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            out.writeInt(Status.ErrorUnknownPokemonType.ordinal());
        }
        catch (SQLException e) {
            out.writeInt(Status.ErrorDatabaseAccess.ordinal());
        }
        finally {
            out.flush();
        }
    }

    private Status AddRun() {
        try {
            PokemonType type = PokemonType.values()[in.readInt()];
            String jsonString = in.readUTF();
            TeamDTO dto = gson.fromJson(jsonString, TeamDTO.class);
            Team team = new Team(dto.pokemonInTeam);
            Run run = new Run(type, team);
            System.out.println("Trying to add run with Type " + run.getMonoType());
            runDAO.createEntry(run, getUserID());
            return Status.AddRunSuccess;
        }
        catch (IOException e) {
            System.err.println("Couldn't read team data");
            return Status.ErrorAddRun;
        }
        catch (SQLException e) {
            System.err.println("Database access error");
            return Status.ErrorAddRun;
        }
    }

    private Status getPokemonMoves(int pokedexID) throws IOException {
        try {
            ArrayList<Move> knownMoves = moveDAO.getMovesOfPokemon(pokedexID);
            KnownMoveDTO dto = new KnownMoveDTO(knownMoves);
            out.writeInt(Status.KnownMoveQuerySuccess.ordinal());
            out.writeUTF(gson.toJson(dto));
            out.flush();
            return Status.KnownMoveQuerySuccess;
        }
        catch (SQLException e) {
            System.err.println("Database access error");
            return Status.ErrorDatabaseAccess;
        }
    }
}
