package Server.DatabaseAccessObjects;

import Battle.Moves.Move;
import Server.Factories.MoveFactory;
import Shared.PokemonType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class MoveDAO implements DAO<Move, String>{
    private Connection connection;


    public MoveDAO(Connection connection) {
        this.connection = connection;
    }

    public synchronized void createEntry(Move from, String name) throws SQLException {
        String[] usedVars = from.usedSQLVariables();
        String queryValues = constructQueryVariables(usedVars);
        String placeholders = constructPlaceholders(usedVars.length);
        PreparedStatement pstmt =  connection.prepareStatement("INSERT INTO Moves " + queryValues + " VALUES " + placeholders);
        from.fillStatement(pstmt);
        pstmt.executeUpdate();
    }

    public synchronized Move get(String name) throws SQLException {
        String sqlQuery = "SELECT * FROM Moves WHERE name = ?";
        PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
        pstmt.setString(1, name);
        ResultSet results =  pstmt.executeQuery();
        results.next();
        int moveType = results.getInt("moveType");
        return MoveFactory.createMove(results, moveType);
    }

    public synchronized void update(Move from, String name) throws SQLException {
        String[] usedVars = from.usedSQLVariables();
        String queryValues = constructQueryVariables(usedVars);
        String placeholders = constructPlaceholders(usedVars.length);
        PreparedStatement pstmt =  connection.prepareStatement("UPDATE Moves " + queryValues + " VALUES " + placeholders + " WHERE Name = ?");
        pstmt.setString(usedVars.length * 2 + 1, name);
        from.fillStatement(pstmt);
        pstmt.executeUpdate();
    }

    public synchronized void delete(String name) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Moves WHERE Name = ?");
        pstmt.setString(1, name);
        pstmt.executeUpdate();
    }

    public synchronized ArrayList<Move> getMovesOfPokemon(int pokedexID) throws SQLException {
        ArrayList<Move> allMoves = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement(
                "SELECT * " +
                    "FROM MoveLearning " +
                    "INNER JOIN Moves USING (Name) " +
                    "WHERE PokedexID = ?"
        );
        pstmt.setInt(1, pokedexID);
        ResultSet results =  pstmt.executeQuery();
        while (results.next()) {
            int moveIDDummy = 0;
            String moveName =  results.getString("Name");
            int movePP = results.getInt("MaxPP");
            int moveAccuracy = results.getInt("Accuracy");
            int typeNum = results.getInt("MoveType");
            Move move = new Move(moveIDDummy, moveName, typeNum, movePP, moveAccuracy);
            allMoves.add(move);
        }

        return allMoves;
    }

    private String constructQueryVariables(String[] usedVars) {
        String variableQuery = "(" + String.join(",", usedVars) + ")";
        return variableQuery;
    }

    private String constructPlaceholders(int numOfVars) {
        String out = "(";
        for (int i = 0; i < numOfVars - 1; i++) {
            out += "?,";
        }
        return out  + "?)";
    }
}
