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



    public void add_reminder(String taskName, LocalDateTime reminderTime) {
        Userr userr = VaadinSession.getCurrent().getAttribute(Userr.class);
        String username = userr.getUsername();
        System.out.println(username);
        Reminder reminder = new Reminder( taskName, reminderTime,username);
        reminderRepository.save(reminder);
    }


    public List<Reminder> getReminderByUsername()
    {
        Userr userr = VaadinSession.getCurrent().getAttribute(Userr.class);
        String username = userr.getUsername();
        return reminderRepository.findByName(username);
    }
}