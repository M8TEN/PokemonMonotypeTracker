package Server.DatabaseAccessObjects;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {

    public static void main (String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db")) {
            // Initialize Pokemon Table
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Pokemon " +
                    "(PokedexID INTEGER PRIMARY KEY," +
                    "Name TEXT NOT NULL," +
                    "TypeA INTEGER NOT NULL," +
                    "TypeB INTEGER," +
                    "HP INTEGER NOT NULL," +
                    "Attack INTEGER NOT NULL," +
                    "Defense INTEGER NOT NULL," +
                    "Special INTEGER NOT NULL," +
                    "Speed INTEGER NOT NULL," +
                    "KnownMoves TEXT)");
            System.out.println("Created Table 'Pokemon'");
            // Initialize Move Table
            String moveSQL = "CREATE TABLE IF NOT EXISTS Moves (" +
                    "Name TEXT PRIMARY KEY, " +
                    "MaxPP INTEGER NOT NULL, " +
                    "Accuracy INTEGER NOT NULL, " +
                    "MoveType INTEGER NOT NULL, " +
                    "Damage INTEGER, " +
                    "Duration INTEGER, " +
                    "TurnsActive INTEGER, " +
                    "DrainPercentage REAL, " +
                    "AffectedStat INTEGER, " +
                    "EffectStrength INTEGER);";
            stmt.executeUpdate(moveSQL);
            System.out.println("Created Table 'Moves'");
            // Initialize User Table
            stmt.execute("CREATE TABLE IF NOT EXISTS Users (UserID INTEGER PRIMARY KEY AUTOINCREMENT, Username TEXT, PasswordHash BLOB)");
            System.out.println("Created Table 'Users'");
            // Initialize Runs Table
            stmt.execute("CREATE TABLE IF NOT EXISTS Runs (RunID INTEGER PRIMARY KEY AUTOINCREMENT, Type INTEGER NOT NULL, " +
                    "Pokemon1 INTEGER REFERENCES Pokemon(PokedexID), " +
                    "Pokemon2 INTEGER REFERENCES Pokemon(PokedexID), " +
                    "Pokemon3 INTEGER REFERENCES Pokemon(PokedexID), " +
                    "Pokemon4 INTEGER REFERENCES Pokemon(PokedexID), " +
                    "Pokemon5 INTEGER REFERENCES Pokemon(PokedexID), " +
                    "Pokemon6 INTEGER REFERENCES Pokemon(PokedexID), " +
                    "Beaten INTEGER NOT NULL, " +
                    "UserID INTEGER REFERENCES Users(UserID))");
            System.out.println("Created Table 'Runs'");

            // Initialize MoveLearning Table
            stmt.execute("CREATE TABLE IF NOT EXISTS MoveLearning (PokedexID INTEGER REFERENCES Pokemon, MoveID TEXT REFERENCES Moves, PRIMARY KEY (PokedexID, MoveID))");
            System.out.println("Created Table 'MoveLearning'");
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
