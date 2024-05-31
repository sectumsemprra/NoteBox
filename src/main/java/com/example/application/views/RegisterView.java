package com.example.application.views;

import com.example.application.services.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@AnonymousAllowed
@Route("register")
public class RegisterView extends Composite<Component> {

    private final AuthService authService;

    public RegisterView(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Component initContent() {
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");

        Button sendButton = new Button("Submit", event -> register(
                username.getValue(),
                password1.getValue(),
                password2.getValue()
        ));

        VerticalLayout layout = new VerticalLayout(
                new H2("Register"),
                username,
                password1,
                password2,
                sendButton
        );
        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return layout;
    }

    private void register(String username, String password1, String password2) {
        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if (password1.isEmpty()) {
            Notification.show("Enter a password");
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {
            authService.register(username, password1);
            //suserService.saveSUser(suser);

            Notification.show("Registered");
            UI.getCurrent().navigate("/");
        }
    }
}
