
package com.example.application.views;
import com.example.application.data.Userr;
import com.example.application.services.AuthService;
import com.example.application.services.UserRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
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

    private final AuthService authServicee;
    private final UserRepository userRepository;
    public RegisterView(AuthService authService, UserRepository userRepository) {
        this.authServicee = authService;
        this.userRepository = userRepository;
    }

    @Override
    protected Component initContent() {
        TextField username = new TextField("Username");
        TextField institute = new TextField("Institute");
        PasswordField password1 = new PasswordField("Password");
        PasswordField password2 = new PasswordField("Confirm password");
        return new VerticalLayout(
                new H2("Register"),
                username,
                institute,
                password1,
                password2,

                new Button("Submit", event-> register(
                        username.getValue(),
                        password1.getValue(),
                        password2.getValue(),
                        institute.getValue()
                ))
        );
    }


    private void register(String username, String password1, String password2, String institute) {
        if (username.trim().isEmpty()) {
            Notification.show("Enter a username");
        } else if (password1.isEmpty()) {
            Notification.show("Enter a password");
        } else if (!password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {

            Userr alreadyExists = userRepository.getByUsername(username);

            if(alreadyExists == null) {
                authServicee.register(username, password1, institute);
                Notification.show("Registered");
                UI.getCurrent().navigate("/");
            }
            else
            {
                Notification.show("Username already exists");
            }

        }
    }
}

