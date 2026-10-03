package Server.DatabaseAccessObjects;

import java.sql.*;
import java.util.ArrayList;

import Shared.Pokemon;
import Shared.PokemonType;

public class PokemonDAO implements DAO<Pokemon, Integer> {
    private Connection connection;
    private final String TABLE_NAME = "Pokemon";

    public PokemonDAO(Connection connection) {
        this.connection = connection;
    }

    public synchronized void createEntry(Pokemon from, Integer id) throws SQLException {
        String sqlStr = "INSERT INTO " + TABLE_NAME + " VALUES (?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt = connection.prepareStatement(sqlStr);
        pstmt.setInt(1, from.getPokedexID());
        pstmt.setString(2, from.getName());
        pstmt.setInt(3, from.getPrimaryType().ordinal());
        PokemonType secondaryType = from.getSecondaryType();
        if (secondaryType == PokemonType.None) {pstmt.setNull(4, Types.INTEGER);}
        else {pstmt.setInt(4, secondaryType.ordinal());}
        int[] baseValues = from.getBaseStats();
        pstmt.setInt(5, baseValues[0]);
        pstmt.setInt(6, baseValues[1]);
        pstmt.setInt(7, baseValues[2]);
        pstmt.setInt(8, baseValues[3]);
        pstmt.setInt(9, baseValues[4]);
        pstmt.setString(10, "");
        pstmt.execute();
    }

    public synchronized Pokemon get(Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Pokemon WHERE PokedexID = ?");
        pstmt.setInt(1, id);
        ResultSet results = pstmt.executeQuery();
        results.next();
        return getPokemonFromResult(results);
    }

    public synchronized void update(Pokemon newData, Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("UPDATE Pokemon SET Name = ?, TypeA = ?, TypeB = ?, HP = ?, Attack = ?, Defense = ?, Special = ?, Speed = ?, KnownMoves = ? WHERE PokedexID = ?");
        pstmt.setString(1, newData.getName());
        pstmt.setInt(2, newData.getPrimaryType().ordinal());
        if (newData.getSecondaryType() == PokemonType.None) {pstmt.setNull(3, Types.INTEGER);}
        else {pstmt.setInt(3, newData.getSecondaryType().ordinal());}
        int[] baseValues = newData.getBaseStats();
        pstmt.setInt(4,baseValues[0]);
        pstmt.setInt(5,baseValues[1]);
        pstmt.setInt(6,baseValues[2]);
        pstmt.setInt(7,baseValues[3]);
        pstmt.setInt(8,baseValues[4]);
        pstmt.setString(9, "");
        pstmt.setInt(10, id);
        pstmt.execute();
    }

    public synchronized void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM Pokemon WHERE PokedexID = " + id;
        Statement stmt = connection.createStatement();
        stmt.execute(sql);
    }

    public synchronized ArrayList<Pokemon> getPokemonByType(PokemonType type) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Pokemon WHERE TypeA = ? OR TypeB = ?");
        pstmt.setInt(1, type.ordinal());
        pstmt.setInt(2, type.ordinal());
        ResultSet results = pstmt.executeQuery();
        if (!results.isBeforeFirst()) return new ArrayList<Pokemon>();

        ArrayList<Pokemon> allPokemon = new ArrayList<Pokemon>();
        while (results.next()) {
            allPokemon.add(getPokemonFromResult(results));
        }

        return allPokemon;
    }

    public synchronized ArrayList<Pokemon> getAllPokemon() throws SQLException {
        ArrayList<Pokemon> allPokemon = new ArrayList<>();
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Pokemon");
        ResultSet results = pstmt.executeQuery();
        while (results.next()) {
            allPokemon.add(getPokemonFromResult(results));
        }
        return allPokemon;
    }

    private Pokemon getPokemonFromResult(ResultSet results) throws SQLException {
        int pokedexID = results.getInt("PokedexID");
        String pkmnName = results.getString("Name");
        int[] baseStats = new int[5];
        int[] IVs = {0, 0, 0, 0, 0};
        baseStats[0] = results.getInt("HP");
        baseStats[1] = results.getInt("Attack");
        baseStats[2] = results.getInt("Defense");
        baseStats[3] = results.getInt("Special");
        baseStats[4] = results.getInt("Speed");
        PokemonType primaryType = PokemonType.values()[results.getInt("TypeA")];
        PokemonType secondaryType = PokemonType.values()[results.getInt("TypeB")];
        if (results.wasNull()) {
            secondaryType = PokemonType.None;
        }
        return new Pokemon(pokedexID, pkmnName, primaryType, secondaryType, 50, baseStats, IVs);
    }

}