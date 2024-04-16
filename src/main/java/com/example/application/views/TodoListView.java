package com.example.application.views.list;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.Route;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Route("todo-list")
public class TodoListView extends VerticalLayout {

    private List<Task> tasks;

    public TodoListView() {
        tasks = new ArrayList<>();

        Button addButton = new Button("Add Task");
        addButton.addClickListener(event -> addTask());

        add(addButton);
    }

    private void addTask() {
        Task task = new Task();
        tasks.add(task);
        add(task);
        task.scheduleReminder(); // Call scheduleReminder for the newly added task
    }


    private class Task extends VerticalLayout {
        private TextField descriptionField;
        private DatePicker reminderDate;
        private TimePicker reminderTime;
        private Button doneButton;

        public Task() {
            descriptionField = new TextField();
            descriptionField.setPlaceholder("Write Down Task");

            reminderDate = new DatePicker();
            reminderDate.setLabel("Reminder Date");

            reminderTime = new TimePicker();
            reminderTime.setLabel("Reminder Time");

           // reminderTime.setStep(Duration.ofMinutes(2));

            doneButton = new Button("Done");
            doneButton.addClickListener(event -> removeTask(this));

            add(descriptionField, reminderDate, reminderTime, doneButton);
        }

        public String getDescription() {
            return descriptionField.getValue();
        }

        public LocalDateTime getReminderDateTime() {
            if (reminderDate.getValue() != null && reminderTime.getValue() != null) {
                return LocalDateTime.of(reminderDate.getValue(), reminderTime.getValue());
            }
            return null;
        }

        public boolean isValidReminder() {
            LocalDateTime reminderDateTime = getReminderDateTime();
            return reminderDateTime == null || reminderDateTime.isAfter(LocalDateTime.now());
        }


        public void scheduleReminder() {
            LocalDateTime reminderDateTime = getReminderDateTime();
            if (isValidReminder() && reminderDateTime != null) {
                Duration duration = Duration.between(LocalDateTime.now(), reminderDateTime);
                long delayMillis = duration.toMillis();
                if (delayMillis > 0) {
                    Notification notification = new Notification("Reminder: " + getDescription(), 5000);
                    notification.open();
                }
            } else {
                Notification.show("Reminder date and time must be in the future");
            }
        }
    }


    private void removeTask(Task task) {
        remove(task);
        tasks.remove(task);
    }
}
