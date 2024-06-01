package com.example.application.views.list;

import com.example.application.data.Company;
import com.example.application.data.Contact;
import com.example.application.data.Status;
import com.example.application.entity.FileEntity;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

public class FileForm extends FormLayout {
    Binder<FileEntity> binder = new BeanValidationBinder<>(FileEntity.class);
    TextField fileTitle = new TextField("File Title");


    TextField username = new TextField("Username");
    TextField userInstitute = new TextField("Institute");
   // TextField uploadDate = new TextField("Upload Date");



   // Button save = new Button("Save");
    //Button delete = new Button("Delete");
    Button close = new Button("Close");


    public FileForm(List<Company> companies, List<Status> statuses) {
        addClassName("contact-form");
        binder.bindInstanceFields(this);

//        company.setItems(companies);
//        company.setItemLabelGenerator(Company::getName);
//        status.setItems(statuses);
//        status.setItemLabelGenerator(Status::getName);

        fileTitle.setReadOnly(true);
        username.setReadOnly(true);
        userInstitute.setReadOnly(true);

        add(fileTitle,
                username,
                userInstitute,
              //  uploadDate,
                createButtonsLayout());
    }


    // other methods and fields omitted

    private FileEntity fileEntity;

    public void setFileEntity(FileEntity fileEntity) {
        this.fileEntity = fileEntity;
        binder.readBean(fileEntity);
    }

    private HorizontalLayout createButtonsLayout() {
       // save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
       // delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    //    save.addClickListener(event -> validateAndSave());
  //      delete.addClickListener(event -> fireEvent(new DeleteEvent(this, contact)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

       // binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        //save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ENTER);

        return new HorizontalLayout(close);
    }
    private void validateAndSave() {
//        try {
//            binder.writeBean(contact);
//            fireEvent(new SaveEvent(this, contact));
//        } catch (ValidationException e) {
//            e.printStackTrace();
//        }
    }

    // Events
    public static abstract class FileFormEvent extends ComponentEvent<FileForm> {
        private FileEntity fileEntity;

        protected FileFormEvent(FileForm source, FileEntity fileEntity) {
            super(source, false);
            this.fileEntity = fileEntity;
        }

        public FileEntity getFileEntity() {
            return fileEntity;
        }
    }

//    public static class SaveEvent extends ContactFormEvent {
//        SaveEvent(ContactForm source, Contact contact) {
//            super(source, contact);
//        }
//    }

//    public static class DeleteEvent extends ContactFormEvent {
//        DeleteEvent(ContactForm source, Contact contact) {
//            super(source, contact);
//        }
//
//    }

    public static class CloseEvent extends FileFormEvent {
        CloseEvent(FileForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}