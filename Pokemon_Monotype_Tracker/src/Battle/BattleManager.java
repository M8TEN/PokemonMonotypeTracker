package Battle;

import Server.Factories.PokemonFactory;

import java.util.ArrayList;

public class BattleManager {
    private ArrayList<BattleContext> activeBattles = new ArrayList<BattleContext>();
    private final PokemonFactory pokemonFactory;

    public BattleManager(PokemonFactory pokemonFactory) {
        this.pokemonFactory = pokemonFactory;
    }
}
