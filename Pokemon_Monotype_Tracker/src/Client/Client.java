package Client;

import Battle.Moves.Move;
import Battle.Moves.Stats;
import RunManagement.Run;
import Server.LoginValidator;
import Shared.*;
import Shared.DTOs.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private final Gson gson = new GsonBuilder().create();

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }

    public Client(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
    }

    public static void main(String[] args) {
        try (Socket socket = new Socket(InetAddress.getLocalHost(), 8080)) {
            Client client =  new Client(socket);
            client.interactWithServer();
            client.getIn().close();
            client.getOut().close();
            client.getSocket().close();
        }
        catch (IOException e) {
            System.out.println("Connection to Server failed!");
            System.exit(1);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Status registerNewUser(String username, String password) {
        return registerNewUser(username, LoginValidator.getPasswordHash(password));
    }

    public Pokemon getPokemon(int pokedexID) {
        try {
            out.writeInt(Commands.GetPokemonByID.ordinal());
            out.writeInt(pokedexID);
            out.flush();

            Status status = Status.values()[in.readInt()];

            if (status == Status.PokemonIDQuerySuccess) {
                String pokemonJsonString = in.readUTF();
                PokemonDTO pokemonDTO = gson.fromJson(pokemonJsonString, PokemonDTO.class);
                return pokemonDTO.pokemon;
            }
            else {
                return null;
            }
        }
        catch (IOException e) {
            System.err.println("Could not communicate with server");
            return null;
        }
    }

    private Status registerNewUser(String username, byte[] passwordHash) {
        try {
            out.writeInt(Commands.Register.ordinal());
            LoginDTO loginDTO = new LoginDTO(username, passwordHash);
            out.writeUTF(gson.toJson(loginDTO));
            out.flush();

            int response = in.readInt();
            return Status.values()[response];
        }
        catch (Exception e) {
            return Status.UnknownStatus;
        }
    }

    private void interactWithServer() {
        Scanner scanner = new Scanner(System.in);
        boolean run = true;

        while (run) {
            System.out.print("Enter Command: ");
            String command = scanner.nextLine().strip();
            switch (command) {
                case "Register":
                    LoginDTO dto = queryUserInfo();
                    Status registerStatus = registerNewUser(dto.username, dto.passwordHash);
                    if (registerStatus == Status.RegisterSuccess) {
                        System.out.println("User Registered Successfully!");
                    }
                    else System.out.println("User Registered Failed!");
                    break;

                case "Login":
                    try {
                        dto = queryUserInfo();
                        attemptLogin(dto.username, dto.passwordHash);
                    }
                    catch (Exception e) {
                        System.out.println("Unable to login!");
                    }
                    break;

                case "Create Run": {
                    PokemonType type = queryPokemonType(scanner);
                    ArrayList<Pokemon> availablePokemon = getPokemonOfType(type);
                    System.out.println("Found " + availablePokemon.size() + " Pokemon of Type " + type);
                    for (Pokemon pokemon : availablePokemon) {
                        System.out.println("- " + pokemon.getName());
                    }
                    if (!availablePokemon.isEmpty()) {
                        Team team = createRunTeam(scanner, availablePokemon);
                        System.out.println("Saved new Team with Pokemon: ");
                        for (Pokemon pokemon : team.getPokemonInParty()) {
                            if (pokemon == null) continue;
                            System.out.println(pokemon.getName());
                        }
                        TeamDTO teamDTO = new TeamDTO(team.getPokemonInParty());
                        // Send Run Add request
                        try {
                            out.writeInt(Commands.AddRun.ordinal());
                            out.writeInt(type.ordinal());
                            out.writeUTF(gson.toJson(teamDTO));
                            out.flush();
                        }
                        catch (IOException e) {
                            System.out.println("Couldn't store run");
                        }

                    }
                    break;
                }
                case "Exit" : {
                    run = false;
                    break;
                }
            }
        }
        disconnect();
    }

    public Status addRun(PokemonType type, Team team) {
        try {
            TeamDTO teamDTO = new TeamDTO(team.getPokemonInParty());
            out.writeInt(Commands.AddRun.ordinal());
            out.writeInt(type.ordinal());
            out.writeUTF(gson.toJson(teamDTO));
            out.flush();
            return Status.values()[in.readInt()];

        }
        catch (IOException e) {
            System.out.println("Couldn't store run");
            return Status.ErrorAddRun;
        }
    }

    public void disconnect() {
        try {
            out.writeInt(Commands.Bye.ordinal());
            out.flush();
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Failed to close connection properly");
        }
    }

    private Team createRunTeam(Scanner scanner, ArrayList<Pokemon> availablePokemon) {
        int[] chosenPokemonIndicies =  new int[6];
        Arrays.fill(chosenPokemonIndicies, -1);
        boolean run = true;
        while (run) {
            System.out.println("Available commands are:\nAdd Name\nRemove Name\nList\nSave");
            System.out.println("Enter Command: ");
            String command = scanner.nextLine().strip();
            switch (command) {
                case "Add Name": {
                    boolean found = false;
                    System.out.println("Enter Name: ");
                    String nameToEnter = scanner.nextLine().strip();
                    for (int i = 0; i < availablePokemon.size(); i++) {
                        Pokemon pokemon = availablePokemon.get(i);
                        if (pokemon.getName().equals(nameToEnter)) {
                            // Find open slot
                            int j = 0;
                            while (j < chosenPokemonIndicies.length && chosenPokemonIndicies[j] != -1) j++;
                            if (j >= chosenPokemonIndicies.length) {
                                System.out.println("Too many Pokemon in Party");
                            } else {
                                chosenPokemonIndicies[j] = i;
                                found = true;
                            }
                            break;
                        }
                    }
                    if (!found) {
                        System.out.println("Pokemon with name " + nameToEnter + " does not exist!");
                    } else {
                        System.out.println("Pokemon with name " + nameToEnter + " has been added!");
                        System.out.println("Team is now: ");
                        for (int index = 0; index < chosenPokemonIndicies.length; index++) {
                            int chosenIndex = chosenPokemonIndicies[index];
                            if (chosenIndex == -1) continue;
                            System.out.println("- " + availablePokemon.get(chosenPokemonIndicies[index]).getName());
                        }
                    }
                    break;
                }

                case "Remove Name": {

                    break;
                }
                case "Save": {
                    run = false;
                    break;
                }

                case "List": {
                    for (Pokemon pokemon : availablePokemon) {
                        System.out.println("- " + pokemon.getName());
                    }
                    break;
                }
            }
        }
        Pokemon[] party = new Pokemon[6];
        for (int i = 0; i < 6; i++) {
            int index = chosenPokemonIndicies[i];
            if (index == -1) continue;
            party[i] = availablePokemon.get(chosenPokemonIndicies[i]);
        }
        return new Team(party);
    }

    public ArrayList<Run> requestRuns() {
        try {
            out.writeInt(Commands.GetRuns.ordinal());
            out.flush();

            Status response = Status.values()[in.readInt()];
            if (response == Status.RunQuerySuccess) {
                String dtoJSONString = in.readUTF();
                RunDTO runDTO = gson.fromJson(dtoJSONString, RunDTO.class);
                return runDTO.allRuns;
            }
            else {
                return new ArrayList<>();
            }

        } catch (IOException e) {
            return new ArrayList<Run>();
        }
    }

    public Status attemptLogin(String username, String password) {
        byte[] passwordHash = LoginValidator.getPasswordHash(password);
        return attemptLogin(username, passwordHash);
    }

    private Status attemptLogin(String username, byte[] passwordHash) {
        try {
            LoginDTO dto = new LoginDTO(username, passwordHash);
            String loginJSONString = gson.toJson(dto);
            out.writeInt(Commands.Login.ordinal());
            out.writeUTF(loginJSONString);
            int response = in.readInt();
            if (response < Status.values().length) {
                return Status.values()[response];
            } else {
                return Status.UnknownStatus;
            }
        }
        catch (IOException e) {
            return Status.UnknownStatus;
        }
    }

    private LoginDTO queryUserInfo() {
        Scanner userInput = new Scanner(System.in);
        System.out.print("Username: ");
        String username = userInput.nextLine();
        System.out.print("Password: ");
        String password = userInput.nextLine();
        byte[] hash = LoginValidator.getPasswordHash(password);
        return new LoginDTO(username, hash);
    }

    private PokemonType queryPokemonType(Scanner userInput) {
        boolean correctType = false;
        PokemonType chosenType = PokemonType.None;
        while (!correctType) {
            try {
                System.out.print("Type to filter for: ");
                chosenType =  PokemonType.valueOf(userInput.nextLine());
                correctType = true;
            }
            catch (Exception e) {
                System.out.println("Unknown Pokemon Type");
            }
        }
        return chosenType;
    }

    public ArrayList<Pokemon> getPokemonOfType(PokemonType type) {
        try {
            out.writeInt(Commands.GetPokemonOfType.ordinal());
            out.writeInt(type.ordinal());
            out.flush();

            Status responseStatus = Status.values()[in.readInt()];
            if (responseStatus == Status.TypeQuerySuccess) {
                String responseData = in.readUTF();
                PokemonTypeListDTO dto =  gson.fromJson(responseData, PokemonTypeListDTO.class);
                return dto.getPokemonTypeList();
            }
            else {
                System.out.println(responseStatus);
                return new ArrayList<Pokemon>();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<Pokemon>();
    }

    public ArrayList<Move> getKnownMoves(int pokedexID) {
        try {
            out.writeInt(Commands.GetPokemonMoves.ordinal());
            out.writeInt(pokedexID);
            out.flush();

            Status response = Status.values()[in.readInt()];
            if (response == Status.KnownMoveQuerySuccess) {
                KnownMoveDTO dto = gson.fromJson(in.readUTF(), KnownMoveDTO.class);
                return dto.getKnownMoves();
            }
            else {
                System.out.println(response);
                return new ArrayList<Move>();
            }
        }
        catch (IOException e) {
            System.err.println("Couldn't get known moves");
            return new ArrayList<Move>();
        }
    }

    public Status updateRun(Run run) {
        try {
            ArrayList<Run> runList = new ArrayList<>();
            runList.add(run);
            out.writeInt(Commands.UpdateRun.ordinal());
            out.writeUTF(gson.toJson(new RunDTO(runList)));
            out.flush();

            return Status.values()[in.readInt()];
        }
        catch (IOException e) {
            return Status.ErrorUpdateRun;
        }
    }

    public Status deleteRun(Run run) {
        try {
            ArrayList<Run> runList = new ArrayList<>();
            runList.add(run);
            out.writeInt(Commands.DeleteRun.ordinal());
            out.writeUTF(gson.toJson(new RunDTO(runList)));
            out.flush();

            return Status.values()[in.readInt()];
        }
        catch (IOException e) {
            return Status.ErrorDeleteRun;
        }
    }

}