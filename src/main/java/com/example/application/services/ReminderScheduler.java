package com.example.application.services;

import com.example.application.data.Reminder;
import com.example.application.data.Userr;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
//import org.springframework.security.web.authentication.logout.LogoutSuccessEvent;
import org.springframework.stereotype.Service;

//import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

@Service
public class ReminderScheduler {

    @Autowired
    private ReminderRepository reminderRepository;

    private final ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledTask;

    public ReminderScheduler() {
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.initialize();
    }

    @EventListener
    public void onLogin(InteractiveAuthenticationSuccessEvent event) {
        startScheduler();
    }

    /*@EventListener
    public void onLogout(LogoutSuccessEvent event) {
        stopScheduler();
    }*/

    public void startScheduler() {
        if (scheduledTask == null || scheduledTask.isCancelled()) {
            scheduledTask = taskScheduler.scheduleAtFixedRate(this::checkReminders, 60000);
        }
    }

    public void stopScheduler() {
        if (scheduledTask != null) {
            scheduledTask.cancel(false);
        }
    }

    @Scheduled(fixedRate = 6000)
    public void checkReminders() {
        System.out.println("hahahaha");
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            System.out.println("llllahahaha");
            Userr userr = session.getAttribute(Userr.class);
            if (userr != null) {
                String username = userr.getUsername();
                List<Reminder> reminders = reminderRepository.findByName(username);
                LocalDateTime now = LocalDateTime.now();
                for (Reminder reminder : reminders) {
                    if (reminder.getReminderTime().isBefore(now) || reminder.getReminderTime().isEqual(now)) {
                        triggerReminder(reminder, session);
                    }
                }
            }
        }
    }

    private void triggerReminder(Reminder reminder, VaadinSession session) {
        session.lock();
        try {
            UI.getCurrent().access(() -> {
                Notification.show("Reminder: " + reminder.getReminderName() + " for user: " + reminder.getName());
                reminderRepository.delete(reminder);
            });
        } finally {
            session.unlock();
        }
    }

   /* @PreDestroy
    public void destroy() {
        taskScheduler.shutdown();
    }*/
}
