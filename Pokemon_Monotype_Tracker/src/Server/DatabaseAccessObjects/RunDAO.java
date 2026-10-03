package Server.DatabaseAccessObjects;

import RunManagement.Run;
import Shared.DTOs.RunDTO;
import Shared.Pokemon;
import Shared.PokemonType;
import Shared.Team;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RunDAO implements DAO<Run, Integer> {
    private Connection connection;
    private PokemonDAO pokemonDAO;

    public RunDAO(Connection connection, PokemonDAO pokemonDAO) {
        this.connection = connection;
        this.pokemonDAO = pokemonDAO;
    }

    @Override
    public synchronized void createEntry(Run from, Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Runs (Type, Pokemon1, Pokemon2, Pokemon3, Pokemon4, Pokemon5, Pokemon6, Beaten, UserID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
        pstmt.setInt(1, from.getMonoType().ordinal());
        for (int i = 0; i < from.getPlannedTeam().getPokemonInParty().length; i++) {
            Pokemon currentPokemon =  from.getPlannedTeam().getPokemonInParty()[i];
            if (currentPokemon == null) {
                pstmt.setNull(2 + i, java.sql.Types.INTEGER);
            }
            else {
                pstmt.setInt(2 + i, currentPokemon.getPokedexID());
            }
        }
        pstmt.setBoolean(8, from.isBeaten());
        pstmt.setInt(9, id);
        pstmt.execute();
    }

    @Override
    public synchronized Run get(Integer id) throws SQLException {
        PokemonDAO pokemonDAO = new PokemonDAO(connection);
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Runs WHERE RunID = ?");
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            PokemonType type = PokemonType.values()[rs.getInt("Type")];
            Pokemon[] allPokemon = new Pokemon[6];
            for (int i = 0; i < 6; i++) {
                allPokemon[i] = pokemonDAO.get(rs.getInt("Pokemon" + i));
            }
            Team team = new Team(allPokemon);
            return new Run(type, team);
        }
        return null;
    }

    @Override
    public synchronized void update(Run newData, Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("UPDATE Runs SET (Type, Pokemon1, Pokemon2, Pokemon3, Pokemon4, Pokemon5, Pokemon6, Beaten) = (?, ?, ?, ?, ?, ?, ?, ?) WHERE RunID = ?");
        pstmt.setInt(1, newData.getMonoType().ordinal());
        for (int i = 0; i < newData.getPlannedTeam().getPokemonInParty().length; i++) {
            Pokemon currentPokemon =  newData.getPlannedTeam().getPokemonInParty()[i];
            if (currentPokemon == null) {
                pstmt.setNull(2 + i, java.sql.Types.INTEGER);
            }
            else {
                pstmt.setInt(2 + i, currentPokemon.getPokedexID());
            }
        }
        pstmt.setBoolean(8, newData.isBeaten());
        pstmt.setInt(9, id);
        pstmt.executeUpdate();
    }

    @Override
    public synchronized void delete(Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Runs WHERE RunID = ?");
        pstmt.setInt(1, id);
        pstmt.execute();
    }

    public synchronized RunDTO getAllRuns(Integer userID) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Runs WHERE UserID = ?");
        pstmt.setInt(1, userID);
        ResultSet rs = pstmt.executeQuery();
        ArrayList<Run> runs = new ArrayList<>();
        while (rs.next()) {
            PokemonType type = PokemonType.values()[rs.getInt("Type")];
            Pokemon[] plannedTeam =  new Pokemon[6];
            for (int i = 1; i <= 6; i++) {
                int pokedexID = rs.getInt("Pokemon" + i);
                if (rs.wasNull()) {
                    plannedTeam[i - 1] = null;
                }
                else {
                    plannedTeam[i - 1] = pokemonDAO.get(pokedexID);
                }
            }
            runs.add(new Run(type, new Team(plannedTeam), rs.getInt("RunID")));
        }
        return new RunDTO(runs);
    }
}
