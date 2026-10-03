package Client;

import RunManagement.Run;

import java.util.ArrayList;
import java.util.Objects;

public class UserInfo {
    private final int userID;
    private String username;
    private byte[] passwordHash;
    private ArrayList<Run> runs = new ArrayList<Run>();

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Run> getRuns() {
        return runs;
    }

    public void setRuns(ArrayList<Run> runs) {
        this.runs = runs;
    }

    public UserInfo(int userID, String username, byte[] passwordHash) {
        this.userID = userID;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserInfo userInfo)) return false;
        return getUserID() == userInfo.getUserID();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserID());
    }
}
