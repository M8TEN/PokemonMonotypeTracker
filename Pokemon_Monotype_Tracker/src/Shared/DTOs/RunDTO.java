package Shared.DTOs;

import RunManagement.Run;

import java.util.ArrayList;

public class RunDTO {
    public ArrayList<Run> allRuns;

    public RunDTO() {}

    public RunDTO(ArrayList<Run> allRuns) {
        this.allRuns = allRuns;
    }

}
