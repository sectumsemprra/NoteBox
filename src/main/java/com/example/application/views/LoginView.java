package com.example.application.views;

import com.example.application.services.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import jakarta.annotation.security.PermitAll;
import com.example.application.views.RegisterView;
import com.example.application.services.AuthService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;

import static com.example.application.services.AuthService.getCurrentUsername;
import static com.example.application.services.AuthService.setCurrentUsername;

@Route("/login")
@PageTitle("Login | NoteBox")
@AnonymousAllowed
public class LoginView extends HorizontalLayout {

    public LoginView(AuthService authService) {
        addClassName("plsnospace");
        Div maindiv = new Div();
        maindiv.addClassName("loginbg");
        maindiv.addClassName("container");
        System.out.println("login created");

        //setId("login-view"); //for css classes
        setSizeFull();
        var username = new TextField("Username");
        var password = new PasswordField("Password");
        RouterLink reg = new RouterLink("Register", RegisterView.class);
        H1 mainText = new H1("NoteBox");
        mainText.addClassName("login-name");
        Button loginBtn = new Button("Login",
                event -> {
                    try{
                        authService.authenticate(username.getValue(), password.getValue());
                        setCurrentUsername(username.getValue());
                      //  Notification.show("Welcome " + getCurrentUsername());

                        UI.getCurrent().navigate("/ws");
                    }
                    catch(AuthService.AuthException e){
                        Notification.show("Wrong Credentials");
                    }
                }
        );
        loginBtn.addClassName("custom-button-black");
        RouterLink toreg = new RouterLink("Register", RegisterView.class);
        toreg.addClassName("linkb");

        Div loginContent = new Div();
        loginContent.add(
                mainText,
                username,
                password,
                loginBtn,
                toreg
        );
        loginContent.setId("login-view");

        //loginContent.getStyle().set("padding", "40px");

        maindiv.add(loginContent);
        Image img = new Image("images/loginbg.jpg", "scrawling handwriting");
        img.getStyle().set("margin", "0");
        img.getStyle().set("padding", "0");

        add(img, maindiv);
        img.setWidth("50%");
        maindiv.setWidth("60%");
        setSizeFull();
        setSpacing(false);
    }

}
