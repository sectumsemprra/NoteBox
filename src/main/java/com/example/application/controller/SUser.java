//package com.example.application.controller;
//
//
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//
//@Entity
//@Table(name = "SUserInfo")
//
//public class SUser {
//    @Id
//    @GeneratedValue
//    public int id;
//    public String name;
//    public String username;
//    public String password;
//
//    public SUser() {
//    }
//
//
//    public int getId() {
//        return id;
//    }
//
//    // Setter for id
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    // Getter for name
//    public String getName() {
//        return name;
//    }
//
//    // Setter for name
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    // Getter for username
//    public String getUsername() {
//        return username;
//    }
//
//    // Setter for username
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    // Getter for password
//    public String getPassword() {
//        return password;
//    }
//
//    // Setter for password
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    @Override
//    public String toString() {
//        return "User(id=" + this.getId() + ", name=" + this.getName() + ", username=" + this.getUsername() + ", password=" + this.getPassword() + ")";
//    }
//
//    @Override
//    public int hashCode() {
//        int result = id;
//        result = 31 * result + (name != null ? name.hashCode() : 0);
//        result = 31 * result + (username != null ? username.hashCode() : 0);
//        result = 31 * result + (password != null ? password.hashCode() : 0);
//        return result;
//    }
//
//}
