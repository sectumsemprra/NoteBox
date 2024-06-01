package com.example.application.services;

import com.example.application.data.Userr;
import com.example.application.data.Role;
import com.example.application.views.AdminLayout;
import com.example.application.views.LoginView;
import com.example.application.views.MainLayout;
import com.example.application.views.list.FileUploadView;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;


@Service
public class AuthService {

    public record AuthorizedRoutes(String route, String name, Class<? extends VerticalLayout> views){

    }

    public class AuthException extends Exception{

    }
    private final UserRepository userRepo;
    public static String currentUserName = "";
   // private final ReminderScheduler reminderScheduler;
    public AuthService(UserRepository user){
        this.userRepo = user;
        //this.reminderScheduler=reminderScheduler;
    }

    public void authenticate(String username, String password) throws AuthException{
        Userr userr = userRepo.getByUsername(username);
        if(userr !=null && userr.checkPassword(password)){
            Notification.show(username + " has logged in");
            VaadinSession.getCurrent().getSession().setAttribute("username", username);
            VaadinServletService.getCurrentServletRequest().getSession().setAttribute("name", username);


            currentUserName = username;
            createRoutes(userr.getRole());
        }
        else{
            throw new AuthException();
        }
    }

    private void createRoutes(Role role){

        RouteConfiguration configuration = RouteConfiguration
                .forSessionScope();
        configuration.removeRoute("ws");
        configuration.removeRoute("file");
        configuration.removeRoute("dashboard");


        Notification.show("matched");

        if(role.equals(Role.USER)){
            configuration.setRoute("/ws",
                    ListView.class, MainLayout.class);
            configuration.setRoute("/file", FileUploadView.class, MainLayout.class);
        }
        else if(role.equals(Role.ADMIN)){
            configuration.setRoute("/file",
                    FileUploadView.class, AdminLayout.class);
            configuration.setRoute("/ws",
                    ListView.class, AdminLayout.class);
        }
    }

    public void register(String username, String pass, String institute){
        Userr userr = new Userr(username, pass, Role.USER);
        userr.setInstitute(institute);
        userRepo.save(userr);
        UI.getCurrent().navigate("/");
    }

    public static String getCurrentUsername() {
        return currentUserName;
    }
    public static void setCurrentUsername(String name) {
        currentUserName = name;
    }
    public Userr findByUsername(String currentUserName)
    {
        return userRepo.getByUsername(currentUserName);
    }

    /*public List<AuthorizedRoutes> getAuthorizedRoutes(Role role){
        var routes = new ArrayList<AuthorizedRoutes>();

        if(role.equals(Role.USER)){
            routes.add(new AuthorizedRoutes( "ws", "WorkSpace | NoteBox",  FileUploadView.class));
        }
        else if(role.equals(Role.ADMIN)){
            //routes.add(new AuthorizedRoutes( "workspace", "WorkSpace | NoteBox",  ListView.class));
        }

        return routes;
    }*/

}
