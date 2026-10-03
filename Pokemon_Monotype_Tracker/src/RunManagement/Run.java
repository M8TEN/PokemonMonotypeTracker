package RunManagement;

import Shared.PokemonType;
import Shared.Team;

public class Run {
    private PokemonType monoType;
    private Team plannedTeam;
    private int id;
    private float score = 0.0f;
    private boolean beaten = false;

    public PokemonType getMonoType() {
        return monoType;
    }

    public Team getPlannedTeam() {
        return plannedTeam;
    }

    public void setPlannedTeam(Team plannedTeam) {
        this.plannedTeam = plannedTeam;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public boolean isBeaten() {
        return beaten;
    }

    public void setBeaten(boolean beaten) {
        this.beaten = beaten;
    }

    public Run() {}

    public Run(PokemonType monoType, Team plannedTeam) {
        this.monoType = monoType;
        this.plannedTeam = plannedTeam;
        this.id = -1;
    }

    public Run(PokemonType monoType, Team plannedTeam, int id) {
        this.monoType = monoType;
        this.plannedTeam = plannedTeam;
        this.id = id;
    }
}
