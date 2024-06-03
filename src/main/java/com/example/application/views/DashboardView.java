package com.example.application.views;

import com.example.application.data.Reminder;
import com.example.application.data.Task;
import com.example.application.data.Userr;
import com.example.application.services.*;
import com.example.application.views.MainLayout;
//import com.example.application.views.list.TodoListView;
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
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.List;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
@AnonymousAllowed
public class DashboardView extends VerticalLayout {
    private final CrmService service;

    private final UserRepository userRepo;
    private  final TaskService taskService;
    private final ReminderService reminderService;
    private  final String username;
   // private final AuthService authService;


    public DashboardView(CrmService service,TaskService taskService,ReminderService reminderService, UserRepository userrep) {

        this.userRepo=userrep;
        this.service = service;
        this.taskService=taskService;
        this.reminderService=reminderService;
        //this.authService=authService;
        setPadding(true);
        setSpacing(true);
        setWidthFull();
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
        // To-Do List
        VerticalLayout todoLayout = createSection("To-Do List", "black", false,true);
        todoLayout.getStyle().set("font-family", "Jockey One");
        Userr us = userRepo.getByUsername(username);
        String fullname="";
        if(us!=null){
            fullname= us.getFirstName()+" " +us.getLastName();
        }
        Span usernameSpan = new Span(fullname+"'s Tasks");
        usernameSpan.addClassName("dashboard-name");
        usernameSpan.getStyle().set("margin-right", "auto");

        // Reminders
        VerticalLayout remindersLayout = createSection("Reminders", "black", true,false);
        remindersLayout.getStyle().set("font-family", "Jockey One");

        HorizontalLayout hl = new HorizontalLayout();
        // Add components to layout
        hl.add(todoLayout);
        hl.add(remindersLayout);
        hl.setSizeFull();

        // Align items vertically in the center
        hl.setAlignItems(FlexComponent.Alignment.CENTER);
        add(usernameSpan, hl);
        setSizeFull();
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
           // String username = AuthService.currentUserName;
            System.out.println("task user name is "+ username);
            List<Task> tasks = taskService.getTasksByUsername(username); // Assuming getTasksByTitle() retrieves tasks by title from the service
            for (Task task : tasks) {
                HorizontalLayout taskLayout = createTask(task.getTaskName(), color, showDateTimePicker, tasktime,task.getId(),istask);
                layout.add(taskLayout);
            }
        }
        else {
           // String username = AuthService.currentUserName;
            List<Reminder> reminders = reminderService.getReminderByUsername(username);

            for (Reminder reminder : reminders) {
                System.out.println(reminder.getReminderName());
                HorizontalLayout reminderLayout = createTask(reminder.getReminderName(), color, showDateTimePicker, reminder.getReminderTime(),reminder.getId(),istask);
                layout.add(reminderLayout);
            }
        }
        // Add initial items if available from the service
        //service.getItems(title).forEach(item -> layout.add(createTask(item, color, showDateTimePicker)));

        layout.getStyle().set("border", "1px solid #ccc");
        layout.setSizeFull();
        return layout;
    }

    private HorizontalLayout createTask(String taskName, String color, boolean showDateTimePicker, LocalDateTime time,Long id, boolean isTask) {
        HorizontalLayout taskLayout = new HorizontalLayout();
        taskLayout.setSpacing(true);

        // Checkbox for the task
        Checkbox checkbox = new Checkbox(taskName);
        checkbox.getStyle().set("color", color);
        checkbox.getStyle().set("font-family", "Jeju Myeongjo");
        taskLayout.add(checkbox);

        // DateTimePicker for setting the reminder time (only for reminders)
        if (showDateTimePicker && time != null) {
            // For reminders, show the selected DateTime
            Span timeSpan = new Span("Reminder Time: " + time.toString());
            timeSpan.getStyle().set("font-family", "Jeju Myeongjo");

            taskLayout.add(timeSpan);
        }
        checkbox.addValueChangeListener(event -> {
            if (event.getValue()) {
                if (isTask) {
                    taskService.delete(id);
                } else {
                    reminderService.delete(id);
                }
                // Remove the task layout from the parent layout when checkbox is checked
                taskLayout.getParent().ifPresent(parent -> ((VerticalLayout) parent).remove(taskLayout));
            }
        });


        return taskLayout;
    }

    private void openAddTaskDialog(VerticalLayout layout, String color, boolean showDateTimePicker) {
        Dialog dialog = new Dialog();
        TextField taskField = new TextField("Task");
        // Initialize DateTimePicker for reminders
        DateTimePicker reminderPicker = new DateTimePicker();
        reminderPicker.setLabel("Reminder Time");

        Button saveButton = new Button("Add", event -> {

            if (showDateTimePicker) {
                //String username= AuthService.currentUserName;
               long id= reminderService.add_reminder(taskField.getValue(), reminderPicker.getValue(),username);
                HorizontalLayout task = createTask(taskField.getValue(), color, showDateTimePicker,reminderPicker.getValue(),id,showDateTimePicker);
                layout.add(task);
            } else {
               // String username= AuthService.currentUserName;
               long id= taskService.add_task(taskField.getValue(), username);
                HorizontalLayout task = createTask(taskField.getValue(), color, showDateTimePicker,reminderPicker.getValue(),id,showDateTimePicker);
                layout.add(task);
            }


            dialog.close();
        });
        saveButton.addClassName("custom-button-black");

        dialog.add(taskField);
        if (showDateTimePicker) {
            dialog.add(reminderPicker);
        }
        dialog.add(saveButton);
        dialog.open();
    }
}