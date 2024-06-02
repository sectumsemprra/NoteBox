package com.example.application.services;

import com.example.application.data.Reminder;
import com.example.application.data.Userr;
import com.example.application.data.Role;
import com.example.application.views.AdminLayout;
import com.example.application.views.LoginView;
import com.example.application.views.MainLayout;
import com.example.application.views.list.FileUploadView;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    public record AuthorizedRoutes(String route, String name, Class<? extends VerticalLayout> views){

    }

    public class AuthException extends Exception{

    }
    private final UserRepository userRepo;
    private Userr currentUser;
    //private String instituteName;
    private  final ReminderService reminderService;
    public static String currentUserName = "";
   // private final ReminderScheduler reminderScheduler;
    public AuthService(UserRepository user, ReminderService reminderService){
        this.userRepo = user;
        //this.reminderScheduler=r
        // eminderScheduler;
        this.reminderService=reminderService;
    }

    public void authenticate(String username, String password) throws AuthException{
        Userr userr = userRepo.getByUsername(username);
        if(userr !=null && userr.checkPassword(password)){
            Notification.show(username + " has logged in");
            VaadinSession.getCurrent().getSession().setAttribute("username", username);
            VaadinServletService.getCurrentServletRequest().getSession().setAttribute("name", username);
            VaadinSession vaadinSession = VaadinSession.getCurrent();
            UI ui = UI.getCurrent();
            currentUserName = username;
            createRoutes(userr.getRole());
             currentUser=userr;
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> checkReminder(vaadinSession,ui), 0, 6, TimeUnit.SECONDS);
        }
        else{
            throw new AuthException();
        }
    }
    private void checkReminder(VaadinSession vaadinSession, UI ui) {
        if (vaadinSession != null && ui != null) {
            vaadinSession.access(() -> {
                String username = (String) vaadinSession.getSession().getAttribute("username");
                if (username != null && !username.isEmpty()) {
                    System.out.println("checking1111");
                    System.out.println(username);

                    List<Reminder> reminders = reminderService.getReminderByUsername(username);
                    LocalDateTime now = LocalDateTime.now();

                    for (Reminder reminder : reminders) {
                        if (reminder.getReminderTime().isBefore(now) || reminder.getReminderTime().isEqual(now)) {
                            //reminderService.delete(reminder.getId()); // Remove or mark as sent
                            System.out.println("reminder for you");

                            CompletableFuture.runAsync(() -> ui.access(() -> {
                                Dialog dialog = new Dialog();
                                dialog.add("Reminder for: " + username);

                                Button closeButton = new Button("Close", event -> dialog.close());
                                dialog.add(closeButton);

                                dialog.open();
                            }));
                        }
                    }
                }
            });
        } else {
            System.out.println("VaadinSession or UI is null");
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
    @Transactional
    public void updateUser(String newPassword,String institute) {
        if (!newPassword.trim().isEmpty()) {
            // Create a new user with the updated password and the same username and role
            Userr newUser = new Userr(currentUser.getUsername(), newPassword, currentUser.getRole(),institute);
             System.out.println(currentUser.getUsername()+"heheh");
            // Delete the current user
            userRepo.deleteByUsername(currentUser.getUsername());

            // Save the updated user
            userRepo.save(newUser);
        } else {
            // Handle the case when the new password is empty
            // For example, you can throw an exception or log a message
            System.out.println("New password cannot be empty.");
        }
    }

    public void register(String username, String pass, String institute, String firstName, String lastName){
        Userr userr = new Userr(username, pass, Role.USER);
        userr.setInstitute(institute);
        userr.setFirstName(firstName);
        userr.setLastName(lastName);
        userr.registered = true;
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
    public String getInstituteName()
    {
        return currentUser.getInstitute();
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
