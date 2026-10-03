package Battle.Moves;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HealMove extends Move {
    int healAmount;

    public int getHealAmount() {
        return healAmount;
    }

    public void setHealAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    public HealMove(int moveID, String name, int type, int maxPP, int accuracy, int healAmount) {
        this.healAmount = healAmount;
        super(moveID, name, type, maxPP, accuracy);
    }

    @Override
    public String[] usedSQLVariables() {
        return new String[] {"Name", "MaxPP", "Accuracy", "MoveType", "Damage"};
    }

    @Override
    public void fillStatement(PreparedStatement pstmt) throws SQLException {
        super.fillStatement(pstmt);
        pstmt.setInt(5, healAmount);
    }
}
