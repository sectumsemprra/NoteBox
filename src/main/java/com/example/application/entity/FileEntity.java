package com.example.application.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

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
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "content", columnDefinition = "MEDIUMBLOB")
    public byte[] content;
    public String userInstitute;
    public boolean inDashboard = false;
    public boolean inPublicWorkspace = false;

    public boolean textfile = false;
    public LocalDateTime uploadDate;

    public FileEntity() {
    }

    public FileEntity(int userId, String fileTitle, byte[] fileContent, String username, String txt) {
        //this.fileContent = fileContent;
        this.content = fileContent;
        this.userId = userId;
        this.fileTitle = fileTitle;
        this.username = username;
        if(txt.equals("txt")){
            textfile=true;
        }
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

    public byte[] getFileContent() {
        return content;
    }

    public String getUsername() {
        return username;
    }
    public String getUserInstitute() {
        return userInstitute;
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

    public void setFileContent(byte[] fileContent) {
        this.content = fileContent;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setUserInstitute(String userInstitute) {
        this.userInstitute = userInstitute;
    }
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

}
