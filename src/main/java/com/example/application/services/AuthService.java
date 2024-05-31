package com.example.application.services;

import com.example.application.RegisterView;
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
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
    public AuthService(UserRepository user){
        this.userRepo = user;
    }

    public void authenticate(String username, String password) throws AuthException{
        Userr userr = userRepo.getByUsername(username);
        if(userr !=null && userr.checkPassword(password)){
            VaadinSession.getCurrent().setAttribute(Userr.class, userr);
            //VaadinSession.getCurrent().setAttribute("username", userr.getUsername());
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

        /*RouteConfiguration.forSessionScope().clean();

        getAuthorizedRoutes(role).forEach(
                route-> RouteConfiguration.forSessionScope().setRoute(
                        route.route, route.views, MainLayout.class
                )
        );*/
        Notification.show("matched");


        if(role.equals(Role.USER)){
            configuration.setRoute("/ws",
                    ListView.class, MainLayout.class);
            configuration.setRoute("/login",
                    LoginView.class);
            configuration.setRoute("/register", RegisterView.class);
            configuration.setRoute("/file", FileUploadView.class, MainLayout.class);
        }
        else if(role.equals(Role.ADMIN)){
            configuration.setRoute("/file",
                    FileUploadView.class, AdminLayout.class);
            configuration.setRoute("/ws",
                    ListView.class, AdminLayout.class);
            configuration.setRoute("/login",
                    LoginView.class);
        }

        //configuration.setAnnotatedRoute(ListView.class);

        //UI.getCurrent().getPage().reload();

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


    public void register(String username, String password) {
        Userr user = new Userr(username, password, Role.USER);
        userRepo.save(user);
    }
    public static String getCurrentUsername() {
       return currentUserName;
    }
    public static void setCurrentUsername(String name) {
         currentUserName = name;
    }




}
