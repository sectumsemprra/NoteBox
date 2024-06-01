package com.example.application.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "FileInfo")

public class FileEntity {
    @Id
    @GeneratedValue
    public int id;
    public int userId;
    public String username;
    public String fileTitle;
    public String fileContent;
    public boolean inDashboard = false;
    public boolean inPublicWorkspace = false;

    public FileEntity() {
    }

    public FileEntity(int userId, String fileTitle, String fileContent, String username) {
        this.fileContent = fileContent;
        this.userId = userId;
        this.fileTitle = fileTitle;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getFileTitle() {
        return fileTitle;
    }

    public String getFileContent() {
        return fileContent;
    }

    public String getUsername() {
        return username;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setFileTitle(String fileTitle) {
        this.fileTitle = fileTitle;
    }

    public void setFileContent(String fileContent) {
        this.fileContent = fileContent;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
