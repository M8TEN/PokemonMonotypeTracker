package Client;

import Shared.Pokemon;
import java.util.Optional;
import Battle.Moves.Move;

public class PokemonInfo {
    private Pokemon pokemon;
    private Location[] encounterLocations;
    private Optional<Pokemon> evolvesFrom;
    private int evolvesFromLevel;
    private Optional<Pokemon> evolvesInto;
    private int evolvesIntoLevel;
    private Move[] learnableMoves;

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Location[] getEncounterLocations() {
        return encounterLocations;
    }

    public void setEncounterLocations(Location[] encounterLocations) {
        this.encounterLocations = encounterLocations;
    }

    public Optional<Pokemon> getEvolvesFrom() {
        return evolvesFrom;
    }

    public void setEvolvesFrom(Optional<Pokemon> evolvesFrom) {
        this.evolvesFrom = evolvesFrom;
    }

    public int getEvolvesFromLevel() {
        return evolvesFromLevel;
    }

    public void setEvolvesFromLevel(int evolvesFromLevel) {
        this.evolvesFromLevel = evolvesFromLevel;
    }

    public Optional<Pokemon> getEvolvesInto() {
        return evolvesInto;
    }

    public void setEvolvesInto(Optional<Pokemon> evolvesInto) {
        this.evolvesInto = evolvesInto;
    }

    public int getEvolvesIntoLevel() {
        return evolvesIntoLevel;
    }

    public void setEvolvesIntoLevel(int evolvesIntoLevel) {
        this.evolvesIntoLevel = evolvesIntoLevel;
    }

    public Move[] getLearnableMoves() {
        return learnableMoves;
    }

    public void setLearnableMoves(Move[] learnableMoves) {
        this.learnableMoves = learnableMoves;
    }

    public PokemonInfo(Pokemon pokemon, Location[] encounterLocations, Optional<Pokemon> evolvesFrom, int evolvesFromLevel, Optional<Pokemon> evolvesInto, int evolvesIntoLevel, Move[] learnableMoves) {
        this.pokemon = pokemon;
        this.encounterLocations = encounterLocations;
        this.evolvesFrom = evolvesFrom;
        this.evolvesFromLevel = evolvesFromLevel;
        this.evolvesInto = evolvesInto;
        this.evolvesIntoLevel = evolvesIntoLevel;
        this.learnableMoves = learnableMoves;
    }
}
