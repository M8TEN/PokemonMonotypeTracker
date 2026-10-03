package Battle.Moves;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DrainMove extends MultiTurnMove {
    float drainPercentage;

    public float getDrainPercentage() {
        return drainPercentage;
    }

    public void setDrainPercentage(float drainPercentage) {
        this.drainPercentage = drainPercentage;
    }

    public DrainMove(int moveID, String name, int type, int maxPP, int accuracy, int damage, int duration, int turnsActive, float drainPercentage) {
        this.drainPercentage = drainPercentage;
        super(moveID, name, type, maxPP, accuracy, damage, duration, turnsActive);
    }

    @Override
    public String[] usedSQLVariables() {
        return new String[] {"Name", "MaxPP", "Accuracy", "MoveType", "Damage", "Duration", "TurnsActive", "DrainPercentage"};
    }

    @Override
    public void fillStatement(PreparedStatement pstmt) throws SQLException {
        super.fillStatement(pstmt);
        pstmt.setFloat(8, drainPercentage);
    }
}
