
package com.example.application.views;
import com.example.application.data.Userr;
import com.example.application.services.AuthService;
import com.example.application.services.UserRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;


@Route("register")
@PageTitle("Register | Notebox")
@AnonymousAllowed
public class RegisterView extends HorizontalLayout {

    private final AuthService authServicee;
    private final UserRepository userRepository;
    public RegisterView(AuthService authService, UserRepository userRepository) {
        addClassName("plsnospace");
        this.authServicee = authService;
        this.userRepository = userRepository;
        Div maindiv = new Div();
        maindiv.addClassName("loginbg");
        maindiv.addClassName("container");


        H1 text = new H1("Register");
        text.addClassName("login-name");
        TextField firstName = new TextField("First Name");
        TextField lastName = new TextField("Last Name");
        TextField username = new TextField("Username");
        TextField institute = new TextField("Institute");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        Button reg = new Button("Submit", event-> register(
                username.getValue(),
                password1.getValue(),
                password2.getValue(),
                institute.getValue(),
                firstName.getValue(),
                lastName.getValue()
        ));
        reg.addClassName("custom-button-black");
        RouterLink log = new RouterLink("Login", LoginView.class);
        log.addClassName("linkb");

        Div registercontent = new Div();
        registercontent.add(
                text,
                firstName,
                lastName,
                username,
                institute,
                password1,
                password2,
                reg,
                log
        );
        registercontent.setId("login-view");

        //loginContent.getStyle().set("padding", "40px");

        maindiv.add(registercontent);
        Image img = new Image("images/loginbg.jpg", "scrawling handwriting");
        img.getStyle().set("margin", "0");
        img.getStyle().set("padding", "0");


        add(maindiv,img);
        img.setWidth("50%");
        maindiv.setWidth("60%");
        setSizeFull();
        setSpacing(false);
    }

    private void register(String username, String password1, String password2, String institute, String firstName, String lastName) {
        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if (password1.isEmpty()) {
            Notification.show("Enter a password");
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else if (password1.length() < 6) {
            Notification.show("Password must be at least 6 characters long");
        }else if (firstName.isEmpty()) {
            Notification.show("Enter a First Name");
        }
            else if (lastName.isEmpty()) {
                Notification.show("Enter a Last Name");
            }
            else {

            Userr alreadyExists = userRepository.getByUsername(username);

            if(alreadyExists == null) {
                authServicee.register(username, password1, institute, firstName, lastName);
                Notification.show("Registered");
                UI.getCurrent().navigate("/login");
            }
            else
            {
                Notification.show("Username already exists");
            }

        }
    }
}

