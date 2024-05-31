package com.example.application.services;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    public void showNotification(String message) {
        UI.getCurrent().access(() -> Notification.show(message));
    }
}
