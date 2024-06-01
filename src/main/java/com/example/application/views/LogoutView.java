package com.example.application.views;


import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinSession;

public class LogoutView {
    public LogoutView(){
    }
    public static void logout(){
        UI.getCurrent().getPage().setLocation("/");
        VaadinSession.getCurrent().getSession().invalidate();
        VaadinSession.getCurrent().close();
    }
}
