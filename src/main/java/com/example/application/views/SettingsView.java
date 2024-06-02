package com.example.application.views;

import com.example.application.services.AuthService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
//package com.example.application.views;
import com.example.application.services.AuthService;
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

@Route("settings")
@PageTitle("Settings | Notebox")
@PermitAll
@AnonymousAllowed
public class SettingsView extends Composite {

    private final AuthService authService;

    public SettingsView(AuthService authService) {
        this.authService = authService;
    }

    @Override
    protected Component initContent() {
        String currentUsername;
        String usernam = "";
        Object obj = null;
        VaadinSession vaadinsession = VaadinSession.getCurrent();

        if (vaadinsession != null) {
            obj = vaadinsession.getSession().getAttribute("username");
        }

        if(obj instanceof String){
            usernam = (String) obj;
        }
        currentUsername=usernam;
        // Assume the AuthService has methods to get the current user's details
       // String currentUsername = authService.getCurrentUsername();
       // String currentPassword = authService.getCurrentPassword(); // In practice, you wouldn't retrieve passwords like this

        TextField username = new TextField("Username");
        username.setValue(currentUsername);
        username.setReadOnly(true);
        PasswordField password1 = new PasswordField("New Password");
        PasswordField password2 = new PasswordField("Confirm New Password");
        TextField instituteName = new TextField("Institute Name");
        instituteName.setValue(authService.getInstituteName());
        return new VerticalLayout(
                new H2("Settings"),
                username,
                password1,
                password2,
                instituteName,
                new Button("Save", event -> updateSettings(
                       // username.getValue(),
                        password1.getValue(),
                        password2.getValue(),
                        instituteName.getValue()
                ))
        );
    }

    private void updateSettings( String password1, String password2,String institute) {
        if (!password1.isEmpty() && !password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {
            authService.updateUser( password1, institute);
            Notification.show("Settings updated");
            UI.getCurrent().navigate("/");
        }
    }
}
