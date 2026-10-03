package Client.GUI;

import Shared.Team;

// Class to edit a team
public class TeamBuilder {
    Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public TeamBuilder(Team team) {
        this.team = team;
    }
}
