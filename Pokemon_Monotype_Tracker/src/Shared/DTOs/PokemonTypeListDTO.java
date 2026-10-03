package Shared.DTOs;

import Shared.Pokemon;
import Shared.PokemonType;

import java.util.ArrayList;

public class PokemonTypeListDTO {
    private PokemonType type;
    private ArrayList<Pokemon> pokemonTypeList;

    public PokemonType getType() {
        return type;
    }

    public void setType(PokemonType type) {
        this.type = type;
    }

    public ArrayList<Pokemon> getPokemonTypeList() {
        return pokemonTypeList;
    }

    public void setPokemonTypeList(ArrayList<Pokemon> pokemonTypeList) {
        this.pokemonTypeList = pokemonTypeList;
    }

    public PokemonTypeListDTO() {}

    public PokemonTypeListDTO(PokemonType type, ArrayList<Pokemon> pokemonTypeList) {
        this.type = type;
        this.pokemonTypeList = pokemonTypeList;
    }
}
