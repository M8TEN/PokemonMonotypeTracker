package Battle.Moves;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DamageMove extends Move {
    int damage;

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public DamageMove(int moveID, String name, int type, int maxPP, int accuracy, int damage) {
        this.damage = damage;
        super(moveID, name, type, maxPP, accuracy);
    }

    @Override
    public String[] usedSQLVariables() {
        return new String[] {"Name", "MaxPP", "Accuracy", "MoveType", "Damage"};
    }

    @Override
    public void fillStatement(PreparedStatement pstmt) throws SQLException {
        super.fillStatement(pstmt);
        pstmt.setInt(5, damage);
    }
}
