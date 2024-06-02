package com.example.application.services;

import com.example.application.data.Reminder;
import com.example.application.services.AuthService;
import com.example.application.services.ReminderService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ReminderScheduler {



    private static final Logger logger = LoggerFactory.getLogger(ReminderScheduler.class);

    @Autowired
    private ReminderService reminderService;

    //@Scheduled(fixedRate = 6000) // Runs every minute
    public void checkReminders() {
        String username;
        String usernam = "";
        Object obj = null;
        VaadinSession vaadinsession = VaadinSession.getCurrent();
        if (vaadinsession != null) {
            obj = vaadinsession.getSession().getAttribute("username");
        }
        if(obj instanceof String){
            usernam = (String) obj;
        }
        username=usernam;
        System.out.println("checking");
       // String username = AuthService.currentUserName;
        System.out.println(username);
        if (!username.isEmpty()) {
            List<Reminder> reminders = reminderService.getReminderByUsername(username);
            LocalDateTime now = LocalDateTime.now();

            for (Reminder reminder : reminders) {
                if (reminder.getReminderTime().isBefore(now) || reminder.getReminderTime().isEqual(now)) {
                    sendNotification(reminder);
                    //  reminderService.deleteReminder(reminder.getId()); // Remove or mark as sent
                }
            }
        }
    }

    private void sendNotification(Reminder reminder) {
        // Log the reminder notification
        logger.info("Reminder: " + reminder.getReminderName() + " for " + reminder.getName());
        System.out.println("Reminder: " + reminder.getReminderName() + " for " + reminder.getName());
    }
}
