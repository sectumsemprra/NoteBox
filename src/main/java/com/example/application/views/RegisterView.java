package com.example.application.views;

import com.example.application.services.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("register")
@PageTitle("Register | Notebox")
@AnonymousAllowed
public class RegisterView extends Composite {

    private final AuthService authService;

    public RegisterView(AuthService authService){
        this.authService= authService;
    }
    @Override
    protected Component initContent(){
        TextField username = new TextField("Username");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");


        return new VerticalLayout(
                new H2("Register"),
                username,
                password1,
                password2,
                new Button("Send", event-> register(
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue()
                ))
        );
    }

    private void register(String username, String pass1, String pass2){
        if(username.isEmpty()){
            Notification.show("Enter a username!");
        }
        else if(pass1.isEmpty()){
            Notification.show("Enter a password!");
        }
        else if(!pass1.equals(pass2)){
            Notification.show("Passwords don't match");
        }
        else {
            authService.register(username, pass1);
            Notification.show("Registration successful");
        }
    }
}
