package Battle.Moves;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TrapMove extends MultiTurnMove {

    public TrapMove(int moveID, String name, int type, int maxPP, int accuracy, int damage, int duration, int turnsActive) {
        super(moveID, name, type, maxPP, accuracy, damage, duration, turnsActive);
    }
}
