/*package com.example.application.services;

import com.example.application.data.Userr;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class eminderScheduler {

    @Autowired
    private ReminderScheduler reminderScheduler;

    @Scheduled(fixedRate = 60000) // Check every minute
    public void checkReminders() {
        // Run the reminder check in the UI thread context
        VaadinSession.getCurrent().access(() -> {
            Userr userr = VaadinSession.getCurrent().getAttribute(Userr.class);
            if (userr != null) {
                String username = userr.getUsername();
                reminderScheduler.checkAndTriggerReminders(username);
            }
        });
    }
}*/



