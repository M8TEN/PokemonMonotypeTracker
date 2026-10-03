package DatabaseFill;

import Battle.Moves.Move;
import Server.DatabaseAccessObjects.MoveDAO;
import Server.DatabaseAccessObjects.PokemonDAO;
import Shared.Pokemon;
import Shared.PokemonType;
import com.google.gson.*;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class MoveScraper {

    public static void main(String[] args) {

        try (HttpClient client = HttpClient.newHttpClient(); Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db")) {
            MoveDAO moveDAO = new MoveDAO(conn);
            PokemonDAO pokemonDAO = new PokemonDAO(conn);
            getPokemonBoxSprites(client, pokemonDAO);
            // Get all moves from first Generation
//            fillMoveTable(client, moveDAO);
//            fillMoveLearningTable(client, conn);
            getTypeIcons(client);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getPokemonFrontSprites(HttpClient client, PokemonDAO dao) {
        String upperPath = "Pokemon_Monotype_Tracker/Assets/Pokemon/FrontSprites/";
        String linkExtension = "pokemon/";
        try {
            ArrayList<Pokemon> allPokemon = dao.getAllPokemon();
            for (Pokemon pokemon : allPokemon) {
                String response = getPokeAPIResponse(client, linkExtension + pokemon.getPokedexID());
                JsonObject fullTypeElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject spritesObject = fullTypeElement.get("sprites").getAsJsonObject();
                downloadImage(spritesObject.get("front_default").getAsString(), upperPath + pokemon.getName() + ".png");
            }
        }
        catch (SQLException e) {
            System.err.println("SQL Error, could not acccess Database");
        }
    }

    private static void getPokemonBoxSprites(HttpClient client, PokemonDAO dao) {
        String upperPath = "Pokemon_Monotype_Tracker/Assets/Pokemon/BoxSprites/";
        String linkExtension = "pokemon/";
        try {
            ArrayList<Pokemon> allPokemon = dao.getAllPokemon();
            for (Pokemon pokemon : allPokemon) {
                String response = getPokeAPIResponse(client, linkExtension + pokemon.getPokedexID());
                JsonObject fullTypeElement = JsonParser.parseString(response).getAsJsonObject();
                JsonObject spritesObject = fullTypeElement.get("sprites").getAsJsonObject();
                JsonObject versionsObject = spritesObject.get("versions").getAsJsonObject();
                JsonObject generationObject = versionsObject.get("generation-vii").getAsJsonObject();
                JsonObject iconsObject = generationObject.get("icons").getAsJsonObject();
                downloadImage(iconsObject.get("front_default").getAsString(), upperPath + pokemon.getName() + ".png");
            }
        }
        catch (SQLException e) {
            System.err.println("SQL Error, could not acccess Database");
        }
    }

    private static void getTypeIcons(HttpClient client) {
        String upperPath = "Pokemon_Monotype_Tracker/Assets/Types/";
        String linkExtension = "type/";
        for (int i = 1; i < PokemonType.values().length; i++) {
            PokemonType type =  PokemonType.values()[i];
            String response = getPokeAPIResponse(client, linkExtension + type.toString());
            JsonObject fullTypeElement = JsonParser.parseString(response).getAsJsonObject();
            JsonObject spritesObject = fullTypeElement.get("sprites").getAsJsonObject();
            JsonObject generationObject =  spritesObject.get("generation-iii").getAsJsonObject();
            JsonObject specificGenerationObject = generationObject.get("firered-leafgreen").getAsJsonObject();
            downloadImage(specificGenerationObject.get("name_icon").getAsString(), upperPath + type + ".png");
        }
    }

    private static void downloadImage(String url, String savePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream())) {
            FileOutputStream fileOut = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = in.read(buffer, 0, 1024)) != -1) {
                fileOut.write(buffer, 0, bytesRead);
            }
            fileOut.close();
            System.out.println("Downloaded image to " + savePath);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void fillMoveLearningTable(HttpClient client, Connection conn) {
        for (int pokedexID = 1; pokedexID <= 150; pokedexID++) {
            // Get the Pokemon so we can extract what moves it can learn
            String pokemonResponse = getPokeAPIResponse(client,"pokemon/" + pokedexID);
            JsonObject pokemonObject = JsonParser.parseString(pokemonResponse).getAsJsonObject();
            JsonArray moveArr =  pokemonObject.get("moves").getAsJsonArray();
            for (int i = 0; i < moveArr.size(); i++) {
                JsonObject highMoveObject = moveArr.get(i).getAsJsonObject();
                String detailMoveURL = highMoveObject.get("move").getAsJsonObject().get("url").getAsString();
                String moveJSONString = getPokeAPIFromFullURL(client, detailMoveURL);
                JsonObject moveObject = JsonParser.parseString(moveJSONString).getAsJsonObject();
                JsonObject generationObject = moveObject.get("generation").getAsJsonObject();
                String generationName =  generationObject.get("name").getAsString();
                // Skip the move if it isn't from the first generation
                if (!generationName.equals("generation-i")) continue;

                String formattedMoveName = formatNameForDatabase(moveObject.get("name").getAsString());
                try {
                    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO MoveLearning VALUES (?,?)");
                    pstmt.setInt(1, pokedexID);
                    pstmt.setString(2, formattedMoveName);
                    pstmt.execute();
                    System.out.println("Successfully inserted " + formattedMoveName + " for Pokemon " + pokedexID);
                }
                catch (SQLException e) {
                    System.err.println("Could not add move " + formattedMoveName + " to Table");
                }
            }
        }
    }

    private static void fillMoveTable(HttpClient client, MoveDAO moveDAO) {
        String generationInfo = getPokeAPIResponse(client, "generation/1");
        JsonObject fullObject = JsonParser.parseString(generationInfo).getAsJsonObject();
        JsonArray movesObject = fullObject.get("moves").getAsJsonArray();
        for (int i = 0; i < movesObject.size(); i++) {
            JsonObject moveObject = movesObject.get(i).getAsJsonObject();
            String jsonResponse = getPokeAPIFromFullURL(client, moveObject.get("url").getAsString());
            JsonObject move = JsonParser.parseString(jsonResponse).getAsJsonObject();
            extractValues(moveDAO, move);
        }
    }

    private static String getPokeAPIResponse(HttpClient client, String subLink) {
        final String upperURL = "https://pokeapi.co/api/v2/";
        String fullURL =  upperURL + subLink;
        return getPokeAPIFromFullURL(client, fullURL);
    }

    private static String getPokeAPIFromFullURL(HttpClient client, String link) {
        URI uri = URI.create(link);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        }
        catch (Exception e) {
            return "";
        }
    }

    private static void extractValues(MoveDAO moveDAO, JsonObject moveObject) {
        String moveName = moveObject.get("name").getAsString();
        int pp = moveObject.get("pp").getAsInt();
        JsonObject typeObject = moveObject.get("type").getAsJsonObject();
        String typeName = typeObject.get("name").getAsString();
        String capitalizedTypeName = capitalize(typeName);
        PokemonType type = getTypeByName(capitalizedTypeName);
        int moveAccuracy = (moveObject.get("accuracy").isJsonNull()) ? 100 : moveObject.get("accuracy").getAsInt();
        int moveID = moveObject.get("id").getAsInt();
        String formattedName = formatNameForDatabase(moveName);
        Move move = new Move (moveID, formattedName, type.ordinal(), pp, moveAccuracy);
        try {
            moveDAO.createEntry(move, moveName);
            System.out.println("Created entry for move " + formattedName);
        } catch (SQLException e) {
            System.err.println("Could not create entry for move " + formattedName);
        }
    }

    private static PokemonType getTypeByName(String typeName) {
        try {
            return PokemonType.valueOf(typeName);
        }
        catch (IllegalArgumentException e) {
            return PokemonType.None;
        }
    }

    private static String capitalize(String originalString) {
        return originalString.substring(0, 1).toUpperCase() + originalString.substring(1);
    }

    private static String formatNameForDatabase(String originalName) {
        String[] components =  originalName.split("-");
        String fullOut = capitalize(components[0]);
        for (int i = 1; i < components.length; i++) {
            fullOut += " " + capitalize(components[i]);
        }
        return fullOut;
    }

}
