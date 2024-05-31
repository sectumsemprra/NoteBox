package com.example.application.data;
import jakarta.persistence.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

@Entity

public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    // The initial value is to account for data.sql demo data ids
    @SequenceGenerator(name = "idgenerator", initialValue = 1000)
    private Long id;
    private String username;
    private String taskName;

    public Task(){}
    public Task (String username, String TaskName)
    {
        this.username=username;
        this.taskName=TaskName;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName =taskName ;
    }

}