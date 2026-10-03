package Server.DatabaseAccessObjects;

import Battle.Moves.DamageMove;
import Battle.Moves.DrainMove;
import Battle.Moves.Stats;
import Battle.Moves.StatusMove;
import Client.UserInfo;
import RunManagement.Run;
import Server.LoginValidator;
import Shared.Pokemon;
import Shared.PokemonType;
import Shared.Team;
import Server.DatabaseAccessObjects.MoveDAO;
import Server.DatabaseAccessObjects.UserDAO;
import Server.DatabaseAccessObjects.PokemonDAO;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAOTester {

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db");
            // Construct move mock data
            MoveDAO moveDAO = new MoveDAO(conn);
            DamageMove tackle = new DamageMove(0, "Tackle", PokemonType.Normal.ordinal(), 35, 95, 35);
            StatusMove growl = new StatusMove(1, "Growl", PokemonType.Normal.ordinal(), 40, 100, Stats.ATTACK.ordinal(), -1);
            DrainMove leechseed = new DrainMove(2, "Leech Seed", PokemonType.Grass.ordinal(), 10, 90, 0, -1, -1, 0.125f);
            DamageMove vinewhip = new DamageMove(3, "Vine Whip", PokemonType.Grass.ordinal(), 10, 100, 35);
            DamageMove razorleaf = new DamageMove(4, "Razor Leaf", PokemonType.Grass.ordinal(), 25, 95, 55);
            StatusMove growth = new StatusMove(5, "Growth", PokemonType.Normal.ordinal(), 40, 100, Stats.SPECIAL.ordinal(), 1);
            moveDAO.createEntry(tackle, tackle.getName());
            moveDAO.createEntry(growl, growl.getName());
            moveDAO.createEntry(leechseed, leechseed.getName());
            moveDAO.createEntry(vinewhip, vinewhip.getName());
            moveDAO.createEntry(razorleaf, razorleaf.getName());
            moveDAO.createEntry(growth, growth.getName());

            // Construct Pokemon mock data
//            PokemonDAO pokemonDAO = new PokemonDAO(conn);
//            Pokemon bulbasaur = new Pokemon(1, "Bulbasaur", PokemonType.Grass, PokemonType.Poison, 50, new int[] {45, 49, 49, 65, 45}, new int[5]);
//            Pokemon ivysaur = new Pokemon(2, "Ivysaur", PokemonType.Grass, PokemonType.Poison, 50, new int[] {60, 62, 63, 80, 60}, new int[5]);
//            Pokemon venusaur = new Pokemon(3, "Venusaur", PokemonType.Grass, PokemonType.Poison, 50, new int[] {80, 82, 83, 100, 80}, new int[5]);
//            Pokemon charmander = new Pokemon(4, "Charmander", PokemonType.Fire, PokemonType.None, 50, new int[] {39, 52, 43, 50, 65},  new int[5]);
//            Pokemon charmeleon = new Pokemon(5, "Charmeleon", PokemonType.Fire, PokemonType.None, 50, new int[] {58, 64, 58, 65, 80}, new int[5]);
//            pokemonDAO.createEntry(bulbasaur, bulbasaur.getPokedexID());
//            pokemonDAO.createEntry(ivysaur, ivysaur.getPokedexID());
//            pokemonDAO.createEntry(venusaur, venusaur.getPokedexID());
//            pokemonDAO.createEntry(charmander, charmander.getPokedexID());
//            pokemonDAO.createEntry(charmeleon, charmeleon.getPokedexID());
//
//            // Construct User Mock Data
//            UserDAO userDAO = new UserDAO(conn);
//            for (int i = 0; i < 5; i++) {
//                String username = "User" + Integer.toString(i);
//                UserInfo user = new UserInfo(i, username, LoginValidator.getPasswordHash("KEVIN"));
//                userDAO.createEntry(user, i);
//            }
//
//            RunDAO runDAO = new RunDAO(conn);
//            Run run1 = new Run(PokemonType.Grass, new Team(new Pokemon[] {bulbasaur}));
//            Run run2 = new Run(PokemonType.Grass, new Team(new Pokemon[] {ivysaur}));
//            Run run3 = new Run(PokemonType.Grass, new Team(new Pokemon[] {venusaur}));
//            Run run4 = new Run(PokemonType.Fire, new Team(new Pokemon[] {charmander}));
//            Run run5 = new Run(PokemonType.Fire, new Team(new Pokemon[] {charmeleon}));
//            runDAO.createEntry(run1, 0);
//            runDAO.createEntry(run2, 1);
//            runDAO.createEntry(run3, 2);
//            runDAO.createEntry(run4, 3);
//            runDAO.createEntry(run5, 4);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
