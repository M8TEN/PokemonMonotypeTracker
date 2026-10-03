package Shared.DTOs;

public class LoginDTO {
    public String username;
    public byte[] passwordHash;

    public LoginDTO(String username, byte[] passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public LoginDTO() {}

}
