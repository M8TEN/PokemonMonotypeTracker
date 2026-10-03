package Server.DatabaseAccessObjects;

import Client.UserInfo;

import java.security.MessageDigest;
import java.sql.*;

public class UserDAO implements DAO<UserInfo, Integer> {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public synchronized void createEntry(UserInfo from, Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("INSERT INTO Users (Username, PasswordHash) VALUES (?,?)");
        pstmt.setString(1, from.getUsername());
        pstmt.setBytes(2, from.getPasswordHash());
        pstmt.execute();
    }

    @Override
    public synchronized UserInfo get(Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
        pstmt.setInt(1, id);
        ResultSet results = pstmt.executeQuery();
        results.next();
        String username = results.getString(2);
        byte[] pwHash = results.getBytes(3);
        return new UserInfo(id, username, pwHash);
    }

    public synchronized UserInfo get(String username) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE Username = ?");
        pstmt.setString(1, username);
        ResultSet results = pstmt.executeQuery();
        if (!results.isBeforeFirst()) {return null;}
        results.next();
        int id = results.getInt(1);
        byte[] pwHash = results.getBytes(3);
        return new UserInfo(id, username, pwHash);
    }

    @Override
    public synchronized void update(UserInfo newData, Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("UPDATE Users SET Username = ?, PasswordHash = ? WHERE UserID = ?");
        pstmt.setString(1, newData.getUsername());
        pstmt.setBytes(2, newData.getPasswordHash());
        pstmt.setInt(3, id);
        pstmt.execute();
    }

    @Override
    public synchronized void delete(Integer id) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("DELETE FROM Users WHERE UserID = ?");
        pstmt.setInt(1, id);
        pstmt.execute();
    }

    public synchronized boolean userNameExists(String username) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
        pstmt.setString(1, username);
        ResultSet results = pstmt.executeQuery();
        return results.isBeforeFirst();
    }

    public synchronized byte[] getPasswordHash(String username) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
        pstmt.setString(1, username);
        ResultSet results = pstmt.executeQuery();
        results.next();
        return results.getBytes(3);
    }

    public synchronized int getUserID(String username) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT UserID FROM Users WHERE username = ?");
        pstmt.setString(1, username);
        ResultSet results = pstmt.executeQuery();
        if (!results.isBeforeFirst()) return -1;
        results.next();
        return results.getInt(1);
    }

}
