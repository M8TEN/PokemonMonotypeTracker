package Server.DatabaseAccessObjects;

import Shared.Pokemon;
import Shared.PokemonType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Scanner;

public class FullPokedexInitializer {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:Mydatabase.db")) {
            PokemonDAO pokemonDAO = new PokemonDAO(conn);
            Pokemon[] allPokemon = getPokemonInfo();
            for (Pokemon pokemon: allPokemon) {
                pokemonDAO.createEntry(pokemon, pokemon.getPokedexID());
                System.out.println("Created Entry for " + pokemon.getName());
            }
            System.out.println("Done");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Pokemon[] getPokemonInfo() {
        // Create an instance for EVERY Pokémon from Generation 1
        Pokemon[] allPokemon = new Pokemon[150];
        Scanner scanner = new Scanner(System.in);
        int[] ivs = new int[5];
        for (int i = 0; i < allPokemon.length; i++) {
            int pokedexID = i+1;
            System.out.println("Pokedex ID: " + pokedexID);
            System.out.print("Name: ");
            String name = scanner.nextLine().strip();
            System.out.println();
            System.out.print("Type A: ");
            PokemonType typeA = getNextType(scanner);
            System.out.println();
            System.out.print("Type B: ");
            PokemonType typeB = getNextType(scanner);
            System.out.println();
            System.out.print("HP: ");
            int hp = getNextInt(scanner);
            System.out.println();
            System.out.print("Attack:");
            int attack = getNextInt(scanner);
            System.out.println();
            System.out.print("Defense:");
            int defense = getNextInt(scanner);
            System.out.println();
            System.out.print("Special:");
            int special = getNextInt(scanner);
            System.out.println();
            System.out.print("Speed:");
            int speed = getNextInt(scanner);
            int[] stats = {hp, attack, defense, special, speed};
            Pokemon currentPokemon = new Pokemon(pokedexID, name, typeA, typeB, 50, stats, ivs);
            allPokemon[i] = currentPokemon;
        }
        System.out.println("Last Pokemon finished!");
        return allPokemon;
    }

    public static PokemonType getNextType(Scanner input) {
        boolean correctInput = false;
        PokemonType type = PokemonType.None;
        while (!correctInput) {
            try {
                type = PokemonType.valueOf(input.nextLine().strip());
                correctInput = true;
            }
            catch (Exception e) {
                System.out.println("Invalid type, please try again.");
            }
        }
        return type;
    }

    public static int getNextInt(Scanner input) {
        boolean correctInput = false;
        int out = 0;
        while (!correctInput) {
            try {
                out = Integer.parseInt(input.nextLine().strip());
                correctInput = true;
            }
            catch (Exception e) {
                System.out.println("Invalid number, please try again.");
            }
        }
        return out;
    }

}
