package Shared.DTOs;

import Battle.Moves.Move;

import java.util.ArrayList;

public class KnownMoveDTO {

    private ArrayList<Move> knownMoves;

    public ArrayList<Move> getKnownMoves() {
        return knownMoves;
    }

    public void setKnownMoves(ArrayList<Move> knownMoves) {
        this.knownMoves = knownMoves;
    }

    public KnownMoveDTO() {}

    public KnownMoveDTO(ArrayList<Move> knownMoves) {
        this.knownMoves = knownMoves;
    }

}
