package Shared.DTOs;

import Shared.Pokemon;

public class TeamDTO {
    public Pokemon[] pokemonInTeam;

    public TeamDTO() {}

    public TeamDTO(Pokemon[] pokemonInTeam) {
        this.pokemonInTeam = pokemonInTeam;
    }
}
