package com.example.application.views;

import com.example.application.services.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import jakarta.annotation.security.PermitAll;

@Route("/")
@PageTitle("Login | NoteBox")
@AnonymousAllowed
@CssImport("./Login-view.css")
//@CssImport(value = "./flowcrmtutorial/styles.css")
public class LoginView extends Div {

    public LoginView(AuthService authService) {
        setId("login-view"); //for css classes
        setSizeFull();
        var username = new TextField("Username");
        var password = new PasswordField("Password");
        RouterLink reg = new RouterLink("Register", RegisterView.class);

        add(
                new H1("NoteBox"),
                username,
                password,
                new Button("Login",
                        event -> {
                            try{
                                authService.authenticate(username.getValue(), password.getValue());
                                Notification.show("back to main");
                                UI.getCurrent().navigate("/ws");
                            }
                            catch(AuthService.AuthException e){
                                Notification.show("Wrong Credentials");
                            }
                        }
                        ),
                new RouterLink("Register", RegisterView.class)
        );
    }

}
