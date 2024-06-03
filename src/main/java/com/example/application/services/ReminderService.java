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
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService( ReminderRepository reminderRepository) {

        this.reminderRepository=reminderRepository;
    }



    public Long add_reminder(String taskName, LocalDateTime reminderTime,String username) {
        //String username=AuthService.currentUserName;
        System.out.println(username);
        Reminder reminder = new Reminder( taskName, reminderTime,username);
        reminderRepository.save(reminder);
        return reminder.getId();
    }
    public void delete (Long id)

    {
        reminderRepository.deleteById(id);
    }


    public List<Reminder> getReminderByUsername(String username)
    {
       // String username=AuthService.currentUserName;
        return reminderRepository.findByName(username);
    }
}