package Shared;

import Battle.Moves.Move;

import java.util.Arrays;

public class Team {
    private Pokemon[] pokemonInParty;

    public void setPokemonInParty(Pokemon[] pokemonInParty) {
        this.pokemonInParty = pokemonInParty;
    }

    public Pokemon[] getPokemonInParty() {
        return pokemonInParty;
    }

    public Team() {}

    public Team(Pokemon[] pokemonInParty) {
        this.pokemonInParty = pokemonInParty;
    }

    public void swapOrder(int p1, int p2) {
        Pokemon temp = this.pokemonInParty[p1];
        this.pokemonInParty[p1] = this.pokemonInParty[p2];
        this.pokemonInParty[p2] = temp;
    }

    public boolean isEmpty() {
        return this.pokemonInParty.length == 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        if (this.pokemonInParty.length != team.getPokemonInParty().length) {return false;}
        // Check if both teams have the same Pokémon
        for (int i = 0; i < this.pokemonInParty.length; i++) {
            Pokemon currentPokemon = this.pokemonInParty[i];
            int otherPokemonIdx = hasSamePokemon(team, currentPokemon.getPokedexID());
            if (otherPokemonIdx == -1) {return false;}
            Pokemon otherPokemon = team.getPokemonInParty()[otherPokemonIdx];
            // If both teams have the same Pokémon, check if they know the same moves
            for (int j = 0; j < currentPokemon.getKnownMoves().length; j++) {
                Move ownMove = currentPokemon.getKnownMoves()[j];
                int otherMoveIdx = hasSameMove(otherPokemon, ownMove.getMoveID());
                if (otherMoveIdx == -1) {return false;}
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getPokemonInParty());
    }

    // Returns the index of the other teams Shared.Pokemon with the same Pokédex-ID
    private int hasSamePokemon(Team otherTeam, int pokedexID) {
        for (int i = 0; i < otherTeam.getPokemonInParty().length; i++) {
            Pokemon pokemon =  otherTeam.getPokemonInParty()[i];
            if (pokemon.getPokedexID() == pokedexID) {return i;}
        }
        return -1;
    }

    // Returns the index of the other Pokémon's Battle.Moves.Move with the same Battle.Moves.Move-ID
    private int hasSameMove(Pokemon otherPokemon, int moveID) {
        for (int i = 0; i < otherPokemon.getKnownMoves().length; i++) {
            Move move = otherPokemon.getKnownMoves()[i];
            if (move.getMoveID() != moveID) {return i;}
        }
        return -1;
    }

}
