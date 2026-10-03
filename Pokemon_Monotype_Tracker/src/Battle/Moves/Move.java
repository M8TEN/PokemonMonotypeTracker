package Battle.Moves;

import Battle.CombatElement;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Move implements CombatElement {
    public int moveID;
    private String name;
    private int type;
    private int maxPP;
    private int currentPP;
    private int accuracy;

    public Move() {}

    public Move(int moveID, String name, int type, int maxPP, int accuracy) {
        this.moveID = moveID;
        this.name = name;
        this.type = type;
        this.maxPP = maxPP;
        this.currentPP = maxPP;
        this.accuracy = accuracy;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMaxPP(int maxPP) {
        this.maxPP = maxPP;
    }

    public void setCurrentPP(int currentPP) {
        this.currentPP = currentPP;
    }

    public int getMoveID() {
        return moveID;
    }

    public int getType() {
        return type;
    }

    public int getMaxPP() {
        return maxPP;
    }

    public int getCurrentPP() {
        return currentPP;
    }

    public String getName() {
        return name;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public void effect() {
        if (currentPP <= 0) {
            return;
        }
        currentPP--;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String[] usedSQLVariables() {
        return new String[] {"Name", "MaxPP", "Accuracy", "MoveType"};
    }

    public void fillStatement(PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, name);
        pstmt.setInt(2, maxPP);
        pstmt.setInt(3, accuracy);
        pstmt.setInt(4, type);
    }

}
