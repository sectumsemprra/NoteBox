package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.entity.FileEntity;
import com.example.application.repository.FileRepository;
import com.example.application.service.FileService;
import com.example.application.services.ContactRepository;
import com.example.application.services.CrmService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.apache.catalina.webresources.FileResource;


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
    FileRepository fileRepository;

    public ListView(CrmService service, FileService fileService) {
        this.service = service;
        this.fileService = fileService;
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
        grid.setItems(fileService.getFileEntities());


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
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add notes");
        addContactButton.addClickListener(e -> addContact());
        addContactButton.addClassName("custom-button-black");

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void addContact() {
        grid.asSingleSelect().clear();
        editContact(new Contact());
    }

    private void updateList() {
        //grid.setItems(service.findAllContacts(filterText.getValue()));
    }
}