package com.example.application.services;

//import com.example.application.repositories.TaskRepository;
import com.example.application.data.Reminder;
import com.example.application.data.Task;
import com.example.application.data.Userr;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
   // private final com.example.application.repositories.ReminderRepository reminderRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        //this.reminderRepository=reminderRepository;
    }

    public void add_task(String s)
    {
        String username=AuthService.currentUserName;
        Task t = new Task(username, s);
        taskRepository.save(t);
    }



    public List<Task> getTasksByUsername() {

        System.out.println("laa1");
        String username=AuthService.currentUserName;
        return taskRepository.findByUsername(username);
    }

}