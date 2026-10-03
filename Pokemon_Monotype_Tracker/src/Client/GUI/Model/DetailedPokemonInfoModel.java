package Client.GUI.Model;

import Battle.Moves.Move;
import Shared.Pokemon;
import javafx.scene.image.Image;

import java.util.ArrayList;

public class DetailedPokemonInfoModel {
    private Image frontSprite;
    private Pokemon pokemon;
    private ArrayList<Move> knownMoves;

    public Image getFrontSprite() {
        return frontSprite;
    }

    public void setFrontSprite(Image frontSprite) {
        this.frontSprite = frontSprite;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public ArrayList<Move> getKnownMoves() {
        return knownMoves;
    }

    public void setKnownMoves(ArrayList<Move> knownMoves) {
        this.knownMoves = knownMoves;
    }

    public DetailedPokemonInfoModel(Image frontSprite, Pokemon pokemon, ArrayList<Move> knownMoves) {
        this.frontSprite = frontSprite;
        this.pokemon = pokemon;
        this.knownMoves = knownMoves;
    }
}
