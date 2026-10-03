package Client.GUI.Model;

import Shared.Pokemon;
import Shared.PokemonType;

import java.util.ArrayList;

public class FilterScreenModel {

    private PokemonType runType;
    private ArrayList<Pokemon> filteredPokemon;
    private Pokemon[] pokemonInTeam;
    private int runID;

    public PokemonType getRunType() {
        return runType;
    }

    public void setRunType(PokemonType runType) {
        this.runType = runType;
    }

    public ArrayList<Pokemon> getFilteredPokemon() {
        return filteredPokemon;
    }

    public Pokemon[] getPokemonInTeam() {
        return pokemonInTeam;
    }

    public int getRunID() {
        return runID;
    }

    public void setFilteredPokemon(ArrayList<Pokemon> filteredPokemon) {
        this.filteredPokemon = filteredPokemon;
    }

    public FilterScreenModel(PokemonType runType, ArrayList<Pokemon> filteredPokemon) {
        this.runType = runType;
        this.filteredPokemon = filteredPokemon;
        this.pokemonInTeam = new Pokemon[6];
        this.runID = -1;
    }

    public FilterScreenModel(PokemonType runType, ArrayList<Pokemon> filteredPokemon, Pokemon[] pokemonInTeam, int runID) {
        this.runType = runType;
        this.filteredPokemon = filteredPokemon;
        this.pokemonInTeam = pokemonInTeam;
        this.runID = runID;
    }
}
