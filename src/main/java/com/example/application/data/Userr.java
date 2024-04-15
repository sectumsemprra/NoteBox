package com.example.application.data;

import jakarta.persistence.Entity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

@Entity
public class Userr extends AbstractPerson {
    private String username;
    private String passwordSalt;
    private String passwordHash;
    private Role role;
    public Userr(){

    }

    public Userr(String username, String password, Role role){
        this.username = username;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.md5DigestAsHex((password+this.passwordSalt).getBytes());
        this.role = role;
    }

    public boolean checkPassword(String password){
        return DigestUtils.md5DigestAsHex((password+passwordSalt).getBytes()).equals(passwordHash);
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
