package com.example.application.data;

import jakarta.persistence.Entity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

@Entity
public class Userr extends AbstractPerson {
  //  public static int userId;
    private String password;
    private String username;
    private String firstName;
    private String lastName;
    private String institute;
    private String passwordSalt;
    private String passwordHash;
    private Role role;
    public boolean registered = false;

    public Userr(){

    }

    public Userr(String username, String password, Role role,String institute){
        this.username = username;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.md5DigestAsHex((password+this.passwordSalt).getBytes());
        this.role = role;
        this.institute=institute;
        this.password=password;
    }

    public boolean checkPassword(String password){
        return DigestUtils.md5DigestAsHex((password+passwordSalt).getBytes()).equals(passwordHash);
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }
    public String getInstitute() {
        return institute;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password){this.password=password;}
    public void setInstitute(String institute){ this.institute=institute;}
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


    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
}
