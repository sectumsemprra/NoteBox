package com.example.application.data;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity

public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "idgenerator")
    // The initial value is to account for data.sql demo data ids
    @SequenceGenerator(name = "idgenerator", initialValue = 1000)
    private Long id;

    private String name;
    private String reminderName;

    private LocalDateTime reminderTime;
    public Reminder(){ }
    public Reminder(String reminderName, LocalDateTime reminderTime,String name)
    {
        this.reminderName=reminderName;
        this.reminderTime=reminderTime;
        this.name=name;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReminderName() {
        return reminderName;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }
    public void setName(String name){
        this.name=name;
    }
    public String getName()
    {
        return name;
    }
}