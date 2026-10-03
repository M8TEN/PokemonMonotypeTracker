package Client.GUI;

import Client.Location;
import Client.PokemonInfo;

public class InfoDisplay {
    Location examineLocation;
    PokemonInfo examinePokemon;

    public Location getExamineLocation() {
        return examineLocation;
    }

    public void setExamineLocation(Location examineLocation) {
        this.examineLocation = examineLocation;
    }

    public PokemonInfo getExaminePokemon() {
        return examinePokemon;
    }

    public void setExaminePokemon(PokemonInfo examinePokemon) {
        this.examinePokemon = examinePokemon;
    }

    public InfoDisplay(Location examineLocation, PokemonInfo examinePokemon) {
        this.examineLocation = examineLocation;
        this.examinePokemon = examinePokemon;
    }
}
