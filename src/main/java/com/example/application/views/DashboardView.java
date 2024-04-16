package com.example.application.views;

import com.example.application.services.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Vaadin CRM")
@AnonymousAllowed
public class DashboardView extends HorizontalLayout {
    private CrmService service;

    public DashboardView(CrmService service) {
        this.service = service;
        setPadding(true);
        setSpacing(true);
        setWidthFull();

        // To-Do List
        VerticalLayout todoLayout = new VerticalLayout();
        todoLayout.setSpacing(true);
        todoLayout.setPadding(true);
        H2 todoTitle = new H2("To-Do List");
        todoTitle.getStyle().set("color", "blue"); // Set title color
        todoLayout.add(todoTitle);
        todoLayout.add(createCheckbox("Task 1", "blue")); // Custom method to create checkbox with color
        todoLayout.add(createCheckbox("Task 2", "blue"));
        // Add more tasks dynamically here...

        // Reminders
        VerticalLayout remindersLayout = new VerticalLayout();
        remindersLayout.setSpacing(true);
        remindersLayout.setPadding(true);
        H2 remindersTitle = new H2("Reminders");
        remindersTitle.getStyle().set("color", "green"); // Set title color
        remindersLayout.add(remindersTitle);
        remindersLayout.add(createCheckbox("Reminder 1", "green")); // Custom method to create checkbox with color
        remindersLayout.add(createCheckbox("Reminder 2", "green"));
        // Add more reminders dynamically here...

        // Add components to layout
        add(todoLayout);
        add(remindersLayout);

        // Align items vertically in the center
        setAlignItems(Alignment.CENTER);
    }

    private Checkbox createCheckbox(String label, String color) {
        Checkbox checkbox = new Checkbox(label);
        checkbox.getStyle().set("color", color); // Set checkbox color
        return checkbox;
    }
}
