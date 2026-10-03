package Client.GUI.View;

import Shared.PokemonType;
import javafx.scene.image.Image;

import java.nio.file.Paths;

public class SpriteLoader {
    private static final String assetPath = "Pokemon_Monotype_Tracker/Assets/";

    public static Image loadImage(String path) {
        return new Image(path);
    }

    public static Image getPokemonFrontSprite(String name) {
        String path = Paths.get(assetPath + "Pokemon/FrontSprites/" + name + ".png").toUri().toString();
        return loadImage(path);
    }

    public static Image getPokemonBoxSprite(String name) {
        String path = Paths.get(assetPath + "Pokemon/BoxSprites/" + name + ".png").toUri().toString();
        return loadImage(path);
    }

    public static Image getTypeIcon(PokemonType type) {
        String path = Paths.get(assetPath + "/Types/" + type.toString() + ".png").toUri().toString();
        return loadImage(path);
    }

    public static Image getPokeball() {
        String path = Paths.get(assetPath + "Pokeball4030.png").toUri().toString();
        return loadImage(path);
    }
}
