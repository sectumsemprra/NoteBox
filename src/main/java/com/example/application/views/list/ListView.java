package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.data.Userr;
import com.example.application.entity.FileEntity;
import com.example.application.repository.FileRepository;
import com.example.application.service.FileService;
import com.example.application.services.AuthService;
import com.example.application.services.ContactRepository;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.catalina.webresources.FileResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Route(value ="/ws")
@AnonymousAllowed
public class ListView extends VerticalLayout {
    //Grid<Contact> grid = new Grid<>(Contact.class);
    //Grid<Contact> grid = new Grid<>();
    Grid<FileEntity> grid = new Grid<>();
    TextField filterText = new TextField();
    ContactForm form;
    CrmService service;
    ContactRepository cr;

    FileService fileService;
    AuthService authService;
    FileRepository fileRepository;
    public String currentUsername;

    public ListView(CrmService service, FileService fileService, AuthService authService) {
        this.service = service;
        this.fileService = fileService;
        this.authService = authService;


        String username = "";
        Object obj = null;
        // Retrieve the current username
        VaadinSession vaadinsession = VaadinSession.getCurrent();

        if (vaadinsession != null) {
            //Notification.show("okkkk");
            // Retrieve the attribute
            obj =  vaadinsession.getSession().getAttribute("username");
        }

        /*VaadinServletRequest vsr = VaadinServletRequest.getCurrent();
        if (vsr != null) {
            //Notification.show("okkkk");
            obj = vsr.getSession().getAttribute("name");
        }*/
        //else Notification.show("NOT BEING STORED");

        if(obj instanceof String){
            username = (String) obj;
        }
        currentUsername = username;

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        configureForm();

        add(getToolbar(), getContent());
        //updateList();
        closeEditor();

    }
    private void closeEditor()
    {
        form.setContact(null);
        form.setVisible(false);
        removeClassNames("editing");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new ContactForm(service.findAllCompanies(), service.findAllStatuses());
        form.setWidth("25em");

        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(ContactForm.CloseEvent.class, e -> closeEditor());
    }
    private void saveContact(ContactForm.SaveEvent event)
    {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }
    private void deleteContact(ContactForm.DeleteEvent event)
    {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }


    private void configureGrid() {
        //grid.addClassNames("product-tile");
        grid.setSizeFull();

        grid.addComponentColumn(this::createUserTile).setHeader("Users");
        //grid.addComponentColumn(this::createUserTile).setHeader("");


        //grid.setItems(cr.getAll());
        //grid.setItems(fileService.getFileEntities());

        List<FileEntity> temp = fileService.getFileEntities();
        List<FileEntity> toAdd = new ArrayList<>();
        for (FileEntity entity : temp) {
            if (entity.inPublicWorkspace) {
                toAdd.add(entity);
            }
        }
        grid.setItems(toAdd);


        //grid.setColumns("firstName", "lastName", "email");
        /*egrid.addColumn(contact -> contact.getStatus().getName()).setHeader("Status");
        grid.addColumn(contact -> contact.getCompany().getName()).setHeader("Institute");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));*/

     //   grid.asSingleSelect().addValueChangeListener(e -> editContact(e.getValue()));

        /*grid.addColumn(list -> list.get(0)).setHeader("");
        grid.addColumn(list -> list.get(1)).setHeader("");

        ArrayList<String> ar = new ArrayList<>();
        ar.add("moushi");
        ar.add("please work grid");

        grid.setItems(ar);*/

        //grid.addComponentColumn(tiles::new).setHeader("Users");
        //grid.setItems(service.getAllContacts());
        //grid.addComponentColumn(item -> tile);
        //grid.addColumn(tiles::new);

    }

    private VerticalLayout createUserTile(FileEntity user) {
        Image image = new Image("images/notes-icon.png", user.getUsername());
        image.addClassName("product-image");

        Div title = new Div();
        title.setText(user.getFileTitle());
        title.addClassName("product-name");

        Div name = new Div();
        name.setText(user.getUsername() + " " + user.getUserId());
        name.addClassName("product-name");

        Div description = new Div();
        description.setText(user.getUsername());
        description.addClassName("product-description");

        VerticalLayout tile = new VerticalLayout();
        tile.add(title, name, description);
        tile.setSpacing(false);
        tile.setPadding(false);
        //tile.setAlignItems(Alignment.CENTER);

        HorizontalLayout outer = new HorizontalLayout();
        outer.add(image, tile);

        VerticalLayout v = new VerticalLayout();
        v.add(outer);
        v.addClassName("product-tile");

        return v;
    }

    private void editContact(Contact contact) {
        if(contact == null)
        {
            closeEditor();
        }else{
            form.setContact(contact);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by username...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add notes");
        addContactButton.addClickListener(e -> addNotes());
        addContactButton.addClassName("custom-button-black");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }
    private void addNotes() {
        Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();

        TextField fileTitle = new TextField("File Title");
        TextField fileDescription = new TextField("File Description");


        Userr cuser = authService.findByUsername(currentUsername);




        FileBuffer fileBuffer = new FileBuffer();
        Upload upload = new Upload(fileBuffer);
        upload.setAcceptedFileTypes("text/plain");
        upload.setI18n(new UploadI18N().setDropFiles(new UploadI18N.DropFiles().setOne("Drop file here"))
                .setAddFiles(new UploadI18N.AddFiles().setOne("Upload file"))
                .setError(new UploadI18N.Error().setTooManyFiles("You can only upload one file")));

        Button uploadButton = new Button("Upload", event -> {
            String title = fileTitle.getValue();
            String description = fileDescription.getValue();

            String user = cuser.getUsername();
            int id = cuser.getId();

            if (fileBuffer.getInputStream() != null && !title.isEmpty()) {
                String content = null;
                try {
                    content = new String(fileBuffer.getInputStream().readAllBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                FileEntity fileEntity = new FileEntity(id,title, content , user);
                fileEntity.inPublicWorkspace = true;
                fileService.saveFileEntity(fileEntity);
                updateList();
                dialog.close();
                Notification.show("File uploaded successfully");
            } else {
                Notification.show("All fields are required", 3000, Notification.Position.MIDDLE);
            }
        });

        dialogLayout.add(fileTitle, fileDescription, upload, uploadButton);
        dialog.add(dialogLayout);
        dialog.open();
    }


    private void updateList() {
        if(filterText.getValue() == null || filterText.getValue().isEmpty()) {

            List<FileEntity> temp = fileService.getFileEntities();
            List<FileEntity> toAdd = new ArrayList<>();
            for (FileEntity entity : temp) {
                if (entity.inPublicWorkspace) {
                    toAdd.add(entity);
                }
            }
            grid.setItems(toAdd);

          //  grid.setItems(fileService.getFileEntities());
        }
        else {
            List<FileEntity> temp = fileService.getFileEntityByUsername(filterText.getValue());
            List<FileEntity> toAdd = new ArrayList<>();
            for (FileEntity entity : temp) {
                if (entity.inPublicWorkspace) {
                    toAdd.add(entity);
                }
            }
            grid.setItems(toAdd);
        }
    }
}