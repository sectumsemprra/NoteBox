package com.example.application.views;

import com.example.application.data.Reminder;
import com.example.application.data.Task;
import com.example.application.services.CrmService;
import com.example.application.services.ReminderService;
import com.example.application.services.TaskService;
import com.example.application.views.MainLayout;
import com.example.application.views.list.TodoListView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.validation.constraints.Null;
import com.example.application.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;


@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
@AnonymousAllowed
public class DashboardView extends HorizontalLayout {
    private final CrmService service;
    private  final TaskService taskService;
    private final ReminderService reminderService;
    private final NotificationService notificationService;

    public DashboardView(CrmService service,TaskService taskService,ReminderService reminderService,NotificationService notificationService) {

        this.service = service;
        this.taskService=taskService;
        this.reminderService=reminderService;
        this.notificationService=notificationService;
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        // To-Do List
        VerticalLayout todoLayout = createSection("To-Do List", "blue", false,true);

        // Reminders
        VerticalLayout remindersLayout = createSection("Reminders", "green", true,false);

        // Add components to layout
        add(todoLayout);
        add(remindersLayout);

        // Align items vertically in the center
        setAlignItems(FlexComponent.Alignment.CENTER);
    }

    private VerticalLayout createSection(String title, String color, boolean showDateTimePicker,boolean istask) {
        VerticalLayout layout = new VerticalLayout();
        layout.setSpacing(true);
        layout.setPadding(true);

        H2 sectionTitle = new H2(title);
        sectionTitle.getStyle().set("color", color);
        layout.add(sectionTitle);

        // Add a plus button to add new tasks
        Button addButton = new Button("+");
        addButton.addClickListener(event -> openAddTaskDialog(layout, color, showDateTimePicker));

        layout.add(addButton);
        LocalDateTime tasktime = null;
        // Retrieve previously saved tasks from the service
        if(istask) {
            List<Task> tasks = taskService.getTasksByUsername(); // Assuming getTasksByTitle() retrieves tasks by title from the service
            for (Task task : tasks) {
                HorizontalLayout taskLayout = createTask(task.getTaskName(), color, showDateTimePicker, tasktime);
                layout.add(taskLayout);
            }
        }
        else {
            List<Reminder> reminders = reminderService.getReminderByUsername();

            for (Reminder reminder : reminders) {
                System.out.println(reminder.getReminderName());
                HorizontalLayout reminderLayout = createTask(reminder.getReminderName(), color, showDateTimePicker, reminder.getReminderTime());
                layout.add(reminderLayout);
            }
        }
        // Add initial items if available from the service
        //service.getItems(title).forEach(item -> layout.add(createTask(item, color, showDateTimePicker)));

        return layout;
    }

    private HorizontalLayout createTask(String taskName, String color, boolean showDateTimePicker, LocalDateTime time) {
        HorizontalLayout taskLayout = new HorizontalLayout();
        taskLayout.setSpacing(true);

        // Checkbox for the task
        Checkbox checkbox = new Checkbox(taskName);
        checkbox.getStyle().set("color", color);
        taskLayout.add(checkbox);

        // DateTimePicker for setting the reminder time (only for reminders)
        if (showDateTimePicker && time != null) {
            // For reminders, show the selected DateTime
            Span timeSpan = new Span("Reminder Time: " + time.toString());
            taskLayout.add(timeSpan);
        }

        return taskLayout;
    }

    private void openAddTaskDialog(VerticalLayout layout, String color, boolean showDateTimePicker) {
        Dialog dialog = new Dialog();
        TextField taskField = new TextField("Task");

        // Initialize DateTimePicker for reminders
        DateTimePicker reminderPicker = new DateTimePicker();
        reminderPicker.setLabel("Reminder Time");

        Button saveButton = new Button("Add", event -> {
            HorizontalLayout task = createTask(taskField.getValue(), color, showDateTimePicker,reminderPicker.getValue());
            if (showDateTimePicker) {
                reminderService.add_reminder(taskField.getValue(), reminderPicker.getValue());
            } else {
                taskService.add_task(taskField.getValue());
            }
            layout.add(task);
            dialog.close();
        });

        dialog.add(taskField);
        if (showDateTimePicker) {
            dialog.add(reminderPicker);
        }
        dialog.add(saveButton);
        dialog.open();
    }
}