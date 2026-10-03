package Battle;

import java.util.ArrayList;
import Shared.Pokemon;
import Shared.Team;

public class BattleContext {
    int turnOn = 0; // The turn the battle is currently on. Increases after both players made their move
    Team p1Team;
    Team p2Team;
    Pokemon p1ActivePokemon;
    Pokemon p2ActivePokemon;
    ArrayList<CombatElement> turnQueue = new ArrayList<CombatElement>(); // Holds all CombatElements to execute this turn

    public int getTurnOn() {
        return turnOn;
    }

    public Team getP1Team() {
        return p1Team;
    }

    public Team getP2Team() {
        return p2Team;
    }

    public Pokemon getP1ActivePokemon() {
        return p1ActivePokemon;
    }

    public Pokemon getP2ActivePokemon() {
        return p2ActivePokemon;
    }

    public ArrayList<CombatElement> getTurnQueue() {
        return turnQueue;
    }

    public void setTurnOn(int turnOn) {
        this.turnOn = turnOn;
    }

    public void setP1Team(Team p1Team) {
        this.p1Team = p1Team;
    }

    public void setP2Team(Team p2Team) {
        this.p2Team = p2Team;
    }

    public void setP1ActivePokemon(Pokemon p1ActivePokemon) {
        this.p1ActivePokemon = p1ActivePokemon;
    }

    public void setP2ActivePokemon(Pokemon p2ActivePokemon) {
        this.p2ActivePokemon = p2ActivePokemon;
    }

    public void setTurnQueue(ArrayList<CombatElement> turnQueue) {
        this.turnQueue = turnQueue;
    }

    BattleContext(Team p1Team, Team p2Team) {
        this.p1Team = p1Team;
        this.p2Team = p2Team;
        // Each player starts into the battle with the Pokémon at the front of the party
        this.p1ActivePokemon = p1Team.getPokemonInParty()[0];
        this.p2ActivePokemon = p2Team.getPokemonInParty()[0];
    }
}
