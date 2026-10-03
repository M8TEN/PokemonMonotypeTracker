package Battle.Moves;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MultiTurnMove extends DamageMove {
    int duration;
    int turnsActive;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getTurnsActive() {
        return turnsActive;
    }

    public void setTurnsActive(int turnsActive) {
        this.turnsActive = turnsActive;
    }

    public MultiTurnMove(int moveID, String name, int type, int maxPP, int accuracy, int damage, int duration, int turnsActive) {
        this.duration = duration;
        this.turnsActive = turnsActive;
        super(moveID, name, type, maxPP, accuracy, damage);
    }

    @Override
    public String[] usedSQLVariables() {
        return new String[] {"Name", "MaxPP", "Accuracy", "MoveType", "Duration", "TurnsActive"};
    }

    @Override
    public void fillStatement(PreparedStatement pstmt) throws SQLException {
        super.fillStatement(pstmt);
        pstmt.setInt(6, duration);
        pstmt.setInt(7, turnsActive);
    }
}
