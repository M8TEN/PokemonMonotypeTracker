package Server.Factories;

import Server.DatabaseAccess;

public class PokemonFactory {
    private final DatabaseAccess dbAccess;

    public PokemonFactory(DatabaseAccess dbAccess) {
        this.dbAccess = dbAccess;
    }

}
