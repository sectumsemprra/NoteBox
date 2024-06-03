package com.example.application.views;

import com.example.application.services.AuthService;
import com.example.application.services.UserRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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

@Route(value = "/settings")
@PageTitle("Settings | Notebox")
@AnonymousAllowed
public class SettingsView extends HorizontalLayout {

    private final AuthService authService;
    private final UserRepository userRepo;

    public SettingsView(AuthService authService, UserRepository u) {

        this.authService = authService;
        this.userRepo = u;

        Div maindiv = new Div();
        maindiv.addClassName("settingsbg");
        maindiv.addClassName("container");

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

        H1 set = new H1("Settings");
        set.addClassName("login-name");
        TextField username = new TextField("Username");
        username.setValue(currentUsername);
        username.setReadOnly(true);
        PasswordField password1 = new PasswordField("New Password");
        PasswordField password2 = new PasswordField("Confirm New Password");
        TextField instituteName = new TextField("Institute Name");

        String instituteNameValue = (authService != null && authService.getInstituteName() != null) ? authService.getInstituteName() : "";
        instituteName.setValue(instituteNameValue);
        Button savee = new Button("Save", event -> updateSettings(
                // username.getValue(),
                password1.getValue(),
                password2.getValue(),
                instituteName.getValue()
        ));
        savee.addClassName("custom-button-black");
        Button backk = new Button("Go back", event -> UI.getCurrent().navigate("/ws"));
        backk.addClassName("custom-button-black");

        HorizontalLayout forButtons = new HorizontalLayout();
        forButtons.add(savee, backk);
        forButtons.setWidthFull();

        Div setLayout = new Div();
        setLayout.add(
                set,
                username,
                password1,
                password2,
                instituteName,
                forButtons
        );
        setLayout.setId("login-view");

        maindiv.add(setLayout);
        Image img1 = new Image("images/settings.jpg", "scrawling handwriting");
        img1.getStyle().set("margin", "0");
        img1.getStyle().set("padding", "0");

        Image img2 = new Image("images/settings.jpg", "scrawling handwriting");
        img2.getStyle().set("margin", "0");
        img2.getStyle().set("padding", "0");

        add(img1, maindiv, img2);
        img1.setWidth("20%");
        maindiv.setWidth("60%");
        img2.setWidth("20%");
        setSizeFull();
        setSpacing(false);
    }

    private void updateSettings( String password1, String password2,String institute) {
        if (!password1.isEmpty() && !password1.equals(password2)) {
            Notification.show("Passwords don't match");
        } else {
            authService.updateUser( password1, institute);
            Notification.show("Settings updated");
            UI.getCurrent().navigate("/ws");
        }
    }
}
