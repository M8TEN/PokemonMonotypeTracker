package Client.GUI.Controller;

import Client.GUI.View.FilterView;
import Client.GUI.View.SceneManager;

public class DetailedPokemonInfoController {
    private FilterView filterScreen;

    public DetailedPokemonInfoController(FilterView filterScreen) {
        this.filterScreen = filterScreen;
    }

    public void onBackPressed() {
        SceneManager.swapScenes(filterScreen.getMainScene());
    }

}
