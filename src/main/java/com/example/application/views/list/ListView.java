package com.example.application.views.list;

import com.example.application.data.Role;
import com.example.application.data.Userr;
import com.example.application.entity.FileEntity;
import com.example.application.repository.FileRepository;
import com.example.application.service.FileService;
import com.example.application.services.AuthService;
//import com.vaadin.componentfactory.pdfviewer.PdfViewer;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.FileBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Route(value ="/ws")
@AnonymousAllowed
public class ListView extends VerticalLayout {
    //Grid<Contact> grid = new Grid<>(Contact.class);
    //Grid<Contact> grid = new Grid<>();
    Grid<FileEntity> grid = new Grid<>();
    TextField filterText = new TextField();
    ComboBox<String> filterOptions = new ComboBox<>();

    FileForm form;


    FileService fileService;
    AuthService authService;
    FileRepository fileRepository;
    public String currentUsername;
    public FileEntity currentFileEntity;
    public Userr currentUser;
    String uploadedFileName;

    public ListView(FileService fileService, AuthService authService) {

        this.fileService = fileService;
        this.authService = authService;


        //fileService.deleteAll();
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
        currentUser = authService.findByUsername(currentUsername);


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
        form.setFileEntity(null);
        form.setVisible(false);
        removeClassNames("editing");
    }
    private void addToPersonal()
    {
        if(currentFileEntity.inDashboard && currentFileEntity.username.equals(currentUsername))
        {
            Notification.show("Already Added");
        }
        else {
            currentFileEntity.inDashboard = true;
            currentFileEntity.username = currentUsername;
            Userr temp = authService.findByUsername(currentUsername);
            currentFileEntity.userId = temp.getId();


            fileService.updateFileEntity(currentFileEntity);
            Notification.show("Added Successfully");
        }
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
        form = new FileForm();
        form.setWidth("25em");

//        form.addListener(ContactForm.SaveEvent.class, this::saveContact);
//        form.addListener(ContactForm.DeleteEvent.class, this::deleteContact);
        form.addListener(FileForm.CloseEvent.class, e -> closeEditor());

        form.addListener(FileForm.AddToPersonalEvent.class, e -> addToPersonal());
        form.addListener(FileForm.FileViewEvent.class, e -> editFileView());


    }

    private void editFileView() {
        if (currentFileEntity != null && !currentFileEntity.textfile) {

            if(currentFileEntity.getFileContent()==null) Notification.show("file content null");
            else{

                System.out.println(currentFileEntity.getFileTitle()+"'s file size: " + currentFileEntity.getFileContent().length + "bytes");
                //String fileUrl = "/files?title=" + currentFileEntity.getFileTitle();
                String fileUrl = UriComponentsBuilder.fromUriString("/files")
                        .queryParam("id", currentFileEntity.getId())
                        .toUriString();

                Anchor pdfAnchor = new Anchor(fileUrl, "Open PDF");
                pdfAnchor.setTarget("_blank");

                Button viewPdfButton = new Button("View PDF", event -> {
                    getUI().ifPresent(ui -> ui.getPage().open(fileUrl, "_blank"));
                });

                Dialog dialog = new Dialog();
                VerticalLayout dialogLayout = new VerticalLayout();
                Text warning = new Text("Pdf will open in a new tab");
                Button close = new Button("Close");
                close.addClickListener(e-> dialog.close());

                dialogLayout.add(warning,viewPdfButton, close);
                dialog.add(dialogLayout);
                dialog.open();
            }
                    /*PdfViewer pdfViewer = new PdfViewer();
                    StreamResource resource = new StreamResource(currentFileEntity.getFileTitle(),
                            () -> new ByteArrayInputStream(currentFileEntity.getFileContent() ));
                    pdfViewer.setSrc(resource);
                    pdfViewer.openThumbnailsView();
                    add(pdfViewer);
                }*/
                    /*StreamResource streamResource = new StreamResource(currentFileEntity.getFileTitle(),
                        () -> new ByteArrayInputStream(currentFileEntity.getFileContent() ));

                    Anchor pdfAnchor = new Anchor(streamResource, "Open PDF");
                    pdfAnchor.getElement().setAttribute("target", "_blank");

                    Button viewPdfButton = new Button("View PDF", event -> {
                        Page page = getUI().get().getPage();
                        page.open(String.valueOf(streamResource), "_blank");
                    });

                    add(pdfAnchor, viewPdfButton);
                }*/
        } else if (currentFileEntity != null && currentFileEntity.textfile) {
            showTextFileDialog();
        }
        else{
            Notification.show("File Type Not Supported");
        }
    }


    private void showTextFileDialog() {
        String fileContent = fileService.getTextFileContent(currentFileEntity.getId());
        if (fileContent != null) {
            Dialog dialog = new Dialog();

            // Create and configure the TextArea
            TextArea textArea = new TextArea();
            textArea.setReadOnly(true);
            textArea.setValue(fileContent);
            textArea.setWidth("100%");
            textArea.getStyle().set("min-height", "200px");

            // Wrap the TextArea in a Div with fixed height and overflow auto
            Div textAreaContainer = new Div(textArea);
            textAreaContainer.getStyle().set("max-height", "500px");
            textAreaContainer.getStyle().set("overflow", "auto");

            // Create the "Close" button
            Button closeButton = new Button("Close", event -> dialog.close());

            // Create the "Download" button
            StreamResource resource = new StreamResource(currentFileEntity.getFileTitle(), () -> {
                return new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));
            });

            // Add the download anchor to the button's click listener
            Anchor downloadAnchor = new Anchor(resource, "");
            downloadAnchor.getElement().setAttribute("download", true);
            downloadAnchor.getElement().setAttribute("style", "display: none;");
            dialog.add(downloadAnchor);

            Button downloadButton = new Button("Download", event -> {
                downloadAnchor.getElement().callJsFunction("click");
            });

            // Layout for buttons
            HorizontalLayout buttonLayout = new HorizontalLayout();
            buttonLayout.add(closeButton, downloadButton);

            // Add components to the dialog
            dialog.add(textAreaContainer, buttonLayout);
            dialog.setWidth("600px");
            dialog.setHeight("600px");
            dialog.open();
        } else {
            Notification.show("Something went wrong");
        }
    }


    private void configureGrid() {
        //grid.addClassNames("product-tile");
        grid.setSizeFull();

        grid.addComponentColumn(this::createUserTile).setHeader("Notes");
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

        grid.asSingleSelect().addValueChangeListener(e -> editFileForm(e.getValue()));



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
        description.setText(user.getUserInstitute());
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


    private void editFileForm(FileEntity fileEntity) {
        if(fileEntity == null)
        {   closeEditor();
        }else{
            form.setFileEntity(fileEntity);
            form.setVisible(true);
            currentFileEntity = fileEntity;

            addClassName("editing");
        }
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("username/institute");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());


        filterOptions.setItems("username", "institute");
        filterOptions.setPlaceholder("Select filter");

        Button addContactButton = new Button("Add notes");
        addContactButton.addClickListener(e -> addNotes());
        addContactButton.addClassName("custom-button-black");

        Button deleteFileButton = new Button("Delete");
        deleteFileButton.addClassName("custom-button-black");
        deleteFileButton.addClickListener(e -> deleteNotes());

        if(currentUser != null && currentUser.getRole() == Role.ADMIN)
        {
            deleteFileButton.setVisible(true);
        }
        else{
            deleteFileButton.setVisible(false);
        }


        HorizontalLayout toolbar = new HorizontalLayout(filterOptions, filterText, addContactButton, deleteFileButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    private void addNotes() {
        Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();

        TextField fileTitle = new TextField("File Title");
        TextField fileDescription = new TextField("File Description");


        Userr cuser = authService.findByUsername(currentUsername);

        FileBuffer fileBuffer = new FileBuffer();
        Upload upload = new Upload(fileBuffer);
        upload.setAcceptedFileTypes("text/plain", "application/pdf");
        upload.setI18n(new UploadI18N().setDropFiles(new UploadI18N.DropFiles().setOne("Drop file here"))
                .setAddFiles(new UploadI18N.AddFiles().setOne("Upload file"))
                .setError(new UploadI18N.Error().setTooManyFiles("You can only upload one file")));




        upload.addSucceededListener(event -> {
            // Capture the file name when the file is successfully uploaded
            uploadedFileName = event.getFileName();
            System.out.println("Uploaded file name: " + uploadedFileName);
        });
        Button uploadButton = new Button("Upload", event -> {
            //Notification.show("reached upload");

            String title = fileTitle.getValue();
            String substr = uploadedFileName.substring(uploadedFileName.length()-3);
            title += ".";
            title += substr;
            System.out.println("new file name: " + title);
            System.out.println("new file substr: " + substr);


            String description = fileDescription.getValue();

            String user = cuser.getUsername();
            int id = cuser.getId();

            if (fileBuffer.getInputStream() != null && !title.isEmpty()) {
                byte[] contents = null;
                try {
                    //content = new String(fileBuffer.getInputStream().readAllBytes());
                    contents = fileBuffer.getInputStream().readAllBytes();

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                FileEntity fileEntity = new FileEntity(id,title, contents , user, substr);

                fileEntity.inPublicWorkspace = true;
                fileEntity.setUserInstitute(cuser.getInstitute());
                fileEntity.setUploadDate(LocalDateTime.now());
                fileService.saveFileEntity(fileEntity);
                updateList();
                dialog.close();
                System.out.println("uploaded file size: " + contents.length + "bytes");
                System.out.println("classed file size: " + fileEntity.getFileContent().length + "bytes");
                System.out.println("database stored file size: "+ fileService.getFileEntityByTitle(fileEntity.getFileTitle()).getFileContent().length + "BYTES");
                Notification.show("File uploaded successfully");
            } else {
                Notification.show("All fields are required", 3000, Notification.Position.MIDDLE);
            }
        });

        dialogLayout.add(fileTitle, fileDescription, upload, uploadButton);
        dialog.add(dialogLayout);
        dialog.open();
    }

    private void deleteNotes()
    {
        if(currentFileEntity.inDashboard)
        {
            currentFileEntity.inPublicWorkspace = false;
            fileService.updateFileEntity(currentFileEntity);
        }else{
            fileService.deleteFileEntity(currentFileEntity.getId());
        }
        updateList();
    }


    private void updateList() {
        if(filterText.getValue() == null || filterText.getValue().isEmpty() || filterOptions.isEmpty()) {

            List<FileEntity> temp = fileService.getFileEntities();
            List<FileEntity> toAdd = new ArrayList<>();
            for (FileEntity entity : temp) {
                if (entity.inPublicWorkspace) {
                    toAdd.add(entity);
                }
            }
            grid.setItems(toAdd);
        }
        else {

            String filterType = filterOptions.getValue();
            if(filterType.equals("username")) {
                List<FileEntity> temp = fileService.getFileEntityByUsername(filterText.getValue());
                List<FileEntity> toAdd = new ArrayList<>();
                for (FileEntity entity : temp) {
                    if (entity.inPublicWorkspace) {
                        toAdd.add(entity);
                    }
                }
                grid.setItems(toAdd);
            }
            else if (filterType.equals("institute")){
                List<FileEntity> temp = fileService.getFileEntityByUserInstitute(filterText.getValue());
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
}