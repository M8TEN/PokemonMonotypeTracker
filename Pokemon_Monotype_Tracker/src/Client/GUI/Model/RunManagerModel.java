package Client.GUI.Model;

import RunManagement.Run;

import java.util.ArrayList;

public class RunManagerModel {
    private ArrayList<Run> allRuns;

    public ArrayList<Run> getAllRuns() {
        return allRuns;
    }

    public void setAllRuns(ArrayList<Run> allRuns) {
        this.allRuns = allRuns;
    }

    public RunManagerModel(ArrayList<Run> allRuns) {
        this.allRuns = allRuns;
    }
}
