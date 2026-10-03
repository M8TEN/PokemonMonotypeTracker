package Battle;

import Shared.Pokemon;

public class BattleSwap {
    Pokemon nextPokemon;

    public Pokemon getNextPokemon() {
        return nextPokemon;
    }

    public void setNextPokemon(Pokemon nextPokemon) {
        this.nextPokemon = nextPokemon;
    }

    public BattleSwap(Pokemon nextPokemon) {
        this.nextPokemon = nextPokemon;
    }
}
