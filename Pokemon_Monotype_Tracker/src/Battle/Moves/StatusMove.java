package Battle.Moves;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusMove extends Move {
    int effectedStat;
    int effectStrength;

    public int getEffectedStat() {
        return effectedStat;
    }

    public void setEffectedStat(int effectedStat) {
        this.effectedStat = effectedStat;
    }

    public int getEffectStrength() {
        return effectStrength;
    }

    public void setEffectStrength(int effectStrength) {
        this.effectStrength = effectStrength;
    }

    public StatusMove(int moveID, String name, int type, int maxPP, int accuracy, int effectedStat, int effectStrength) {
        this.effectedStat = effectedStat;
        this.effectStrength = effectStrength;
        super(moveID, name, type, maxPP, accuracy);
    }

    @Override
    public String[] usedSQLVariables() {
        return new String[] {"Name", "MaxPP", "Accuracy", "MoveType", "AffectedStat", "EffectStrength"};
    }

    @Override
    public void fillStatement(PreparedStatement pstmt) throws SQLException {
        super.fillStatement(pstmt);
        pstmt.setInt(5, effectedStat);
        pstmt.setInt(6, effectStrength);
    }
}
