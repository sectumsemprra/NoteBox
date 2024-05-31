package com.example.application.services;

import com.example.application.data.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
    List<Reminder> findByName(String reminderName); // Corrected method name to match the field

    List<Reminder> findByReminderTimeLessThanEqual(LocalDateTime reminderTime);
}
