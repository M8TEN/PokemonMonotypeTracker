package Server.Factories;

import Battle.Moves.*;
import Server.DatabaseAccess;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MoveFactory {

    public static Move createMove(ResultSet rs, int type) throws SQLException {
        switch (type) {
            case 0: // Base Move Class
                String moveName = rs.getString("Name");
                int moveMaxPP = rs.getInt("MaxPP");
                int moveAccuracy = rs.getInt("Accuracy");
                int moveType = rs.getInt("MoveType");
                return new Move(0, moveName, moveMaxPP, moveAccuracy, moveType);
            case 1: // Damage Move Class
                moveName = rs.getString("Name");
                moveMaxPP = rs.getInt("MaxPP");
                moveAccuracy = rs.getInt("Accuracy");
                moveType = rs.getInt("MoveType");
                int moveDamage = rs.getInt("Damage");
                return new DamageMove(0, moveName, moveMaxPP, moveAccuracy, moveType, moveDamage);
            case 2: // Multi Turn Move Class
                moveName = rs.getString("Name");
                moveMaxPP = rs.getInt("MaxPP");
                moveAccuracy = rs.getInt("Accuracy");
                moveType = rs.getInt("MoveType");
                moveDamage = rs.getInt("Damage");
                int moveDuration = rs.getInt("Duration");
                int moveTurnsActive = rs.getInt("TurnsActive");
                return new MultiTurnMove(0, moveName, moveMaxPP,  moveAccuracy, moveType, moveDamage, moveDuration, moveTurnsActive);
            case 3: // Drain Move Class
                moveName = rs.getString("Name");
                moveMaxPP = rs.getInt("MaxPP");
                moveAccuracy = rs.getInt("Accuracy");
                moveType = rs.getInt("MoveType");
                moveDamage = rs.getInt("Damage");
                moveDuration = rs.getInt("Duration");
                moveTurnsActive = rs.getInt("TurnsActive");
                float moveDrainPercentage = rs.getFloat("DrainPercentage");
                return new DrainMove(0, moveName, moveMaxPP, moveAccuracy, moveType, moveDamage, moveDuration, moveTurnsActive, moveDrainPercentage);
            case 4: // Trap Move Class
                moveName = rs.getString("Name");
                moveMaxPP = rs.getInt("MaxPP");
                moveAccuracy = rs.getInt("Accuracy");
                moveType = rs.getInt("MoveType");
                moveDamage = rs.getInt("Damage");
                moveDuration = rs.getInt("Duration");
                moveTurnsActive = rs.getInt("TurnsActive");
                return new TrapMove(0, moveName, moveMaxPP, moveAccuracy, moveType, moveDamage, moveDuration, moveTurnsActive);
            case 5: // Heal Move Class
                moveName = rs.getString("Name");
                moveMaxPP = rs.getInt("MaxPP");
                moveAccuracy = rs.getInt("Accuracy");
                moveType = rs.getInt("MoveType");
                int moveHealing = rs.getInt("Damage");
                return new HealMove(0, moveName, moveMaxPP, moveAccuracy, moveType, moveHealing);
            case 6: // Status Move Class
                moveName = rs.getString("Name");
                moveMaxPP = rs.getInt("MaxPP");
                moveAccuracy = rs.getInt("Accuracy");
                moveType = rs.getInt("MoveType");
                int moveAffectedStat = rs.getInt("AffectedStat");
                int moveEffectStrength = rs.getInt("EffectStrength");
                return new StatusMove(0, moveName, moveMaxPP, moveAccuracy, moveType, moveAffectedStat, moveEffectStrength);
            default:
                return null;
        }
    }
}
