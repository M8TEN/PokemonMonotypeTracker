package Client;

import Shared.Pokemon;
import Shared.Terrain;

import java.util.Objects;

public class LocationEncounter {
    private Pokemon pokemon;
    private float chance;
    private Terrain terrain;
    private int lowerLevelBound;
    private int upperLevelBound;

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public float getChance() {
        return chance;
    }

    public void setChance(float chance) {
        this.chance = chance;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public void setTerrain(Terrain terrain) {
        this.terrain = terrain;
    }

    public int getLowerLevelBound() {
        return lowerLevelBound;
    }

    public void setLowerLevelBound(int lowerLevelBound) {
        this.lowerLevelBound = lowerLevelBound;
    }

    public int getUpperLevelBound() {
        return upperLevelBound;
    }

    public void setUpperLevelBound(int upperLevelBound) {
        this.upperLevelBound = upperLevelBound;
    }

    public LocationEncounter(Pokemon pokemon, float chance, Terrain terrain, int lowerLevelBound, int upperLevelBound) {
        this.pokemon = pokemon;
        this.chance = chance;
        this.terrain = terrain;
        this.lowerLevelBound = lowerLevelBound;
        this.upperLevelBound = upperLevelBound;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LocationEncounter that)) return false;
        return Float.compare(getChance(), that.getChance()) == 0 && getLowerLevelBound() == that.getLowerLevelBound() && getUpperLevelBound() == that.getUpperLevelBound() && Objects.equals(getPokemon(), that.getPokemon()) && getTerrain() == that.getTerrain();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPokemon(), getChance(), getTerrain(), getLowerLevelBound(), getUpperLevelBound());
    }
}
