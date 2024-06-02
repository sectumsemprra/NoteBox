package com.example.application.views.list;

import com.example.application.data.Userr;
import com.example.application.entity.FileEntity;
import com.example.application.service.FileService;
import com.example.application.services.AuthService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.util.UriComponentsBuilder;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AnonymousAllowed
@Route(value = "file", layout = MainLayout.class)
public class FileUploadView extends VerticalLayout {

    private final Grid<FileEntity> grid = new Grid<>();
    private final TextArea fileContentTextArea = new TextArea();
    private final Button deleteButton = new Button("Delete");
    private final Button saveButton = new Button("Save");
    private final Button downloadButton = new Button("Download");
    private final Button addToWorkspaceButton = new Button("Add to Workspace");

    Div notesContainer = new Div();
    private final List<FileEntity> fileEntities = new ArrayList<>();
    private final AuthService authService;
    private final FileService fileService;
    private FileEntity fselectedFile;
    private String selectedFileTitle;
    private final String finalUsername;
    private final int finalUserId;

    public FileUploadView(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;

        String username = "";
        Object obj = null;
        VaadinSession vaadinsession = VaadinSession.getCurrent();

        if (vaadinsession != null) {
            obj = vaadinsession.getSession().getAttribute("username");
        }

        if(obj instanceof String){
            username = (String) obj;
        }

        //header for dashboard page
        Span usernameSpan = new Span(username+"'s Home");
        usernameSpan.addClassName("dashboard-name");
        usernameSpan.getStyle().set("margin-right", "auto");

        Span noteText = new Span("Notes");
        noteText.addClassName("dash-small");
        Span spText = new Span("Scratchpad");
        spText.addClassName("dash-small");
        noteText.setWidthFull();


        Button addWorkSpace = new Button("Add to Workspace");
        addWorkSpace.addClassName("custom-button-black");
        addWorkSpace.getStyle().set("margin-left","auto");
        addWorkSpace.getStyle().set("align-items", "center");

        addWorkSpace.addClickListener(e -> {
            if (selectedFileTitle != null) {
                if(!fselectedFile.inPublicWorkspace)
                {
                    fselectedFile.inPublicWorkspace = true;
                    fileService.updateFileEntity(fselectedFile);
                    Notification.show("Added to Workspace");
                }
                else {
                    Notification.show("Already Added");
                }
            } else {
                Notification.show("No file selected to add to workspace.");
            }
        });

        finalUsername = username;
        Userr userr = authService.findByUsername(finalUsername);
        finalUserId = userr.getId();
        //fileContentTextArea.setVisible(false);

        fileContentTextArea.getStyle().set("overflow", "auto");

        /*HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.add(usernameSpan);*/

        //scratchpad
        //fileContentTextArea.setVisible(false);


        //file upload
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();

        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload Files"));
        upload.setAcceptedFileTypes("application/pdf", "text/plain");
        upload.getElement().executeJs("this.shadowRoot.querySelector('vaadin-upload-file').style.display = 'none';");
        upload.setClassName("upload-container");
        upload.getElement().setAttribute("style", "display: flex; justify-content: center; align-items: center;");
        Div dropLabel = new Div();
        dropLabel.setText("Drop files here");
        dropLabel.setClassName("upload-drop-label");
        upload.setDropLabel(dropLabel);

        //retrieve files
        List<FileEntity> existingFiles = fileService.getFileEntityByUsername(username);
        existingFiles.forEach(file -> {
            if(file.inDashboard) {
                fileEntities.add(file);
            }
        });

        ///CODE FOR NOTES HORIZONTAL LAYOUT
        this.notesContainer.addClassName("notes-container");
        this.notesContainer.setText("No notes yet...");
        this.notesContainer.add(createNotesLayout());


        //populate the grid with notes - REPLACE WITH NOTES LAYOUT
        //refreshGrid();

        //add listener to Upload and add to fileEntities
        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            String substr = fileName.substring(fileName.length()-3);
            try {
                //BufferedReader reader = new BufferedReader(new InputStreamReader(buffer.getInputStream(fileName)));
                //String contents = reader.lines().collect(Collectors.joining("\n"));
                byte[] contents = buffer.getInputStream(fileName).readAllBytes();
                String us = finalUsername;

                FileEntity fileEntity = new FileEntity(finalUserId, fileName, contents, us, substr);
                fileEntity.inDashboard = true;
                fileEntity.setUploadDate(LocalDateTime.now());
                fileEntity.setUserInstitute(userr.getInstitute());
                fileService.saveFileEntity(fileEntity);
                fileEntities.add(fileEntity);

                Notification.show(fileEntity.getFileTitle() + " has been uploaded" + fileEntity.textfile);
                notesContainer.removeAll();
                notesContainer.add(createNotesLayout());
                refreshGrid();
                upload.getElement().executeJs("this.files = []");


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        /*grid.addColumn(FileEntity::getFileTitle).setHeader("Uploaded Files");
        grid.addColumn(FileEntity::getUploadDate).setHeader("Upload Date");

        //to do upon selecting each note
        grid.asSingleSelect().addValueChangeListener(event -> {
            FileEntity selectedFile = event.getValue();
            fselectedFile = selectedFile;

            if (selectedFile != null) {
                selectedFileTitle = selectedFile.getFileTitle();
                if(selectedFile.textfile) {
                    fileContentTextArea.setVisible(true);
                    String fileContent = fileService.getTextFileContent(selectedFile.getId());
                    fileContentTextArea.setValue(fileContent);


                }
                else{
                    //Notification.show("Type not supported");
                    fileContentTextArea.setVisible(false);
                    String fileUrl = UriComponentsBuilder.fromUriString("/files")
                            .queryParam("title", fselectedFile.getFileTitle())
                            .toUriString();

                    Anchor pdfAnchor = new Anchor(fileUrl, "Open PDF");
                    pdfAnchor.setTarget("_blank");

                    Button viewPdfButton = new Button("View PDF", event1 -> {
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
            } else {
                selectedFileTitle = null;
                fileContentTextArea.clear();
                fileContentTextArea.setVisible(false);
            }
        });*/


        downloadButton.addClickListener(e -> {
            if (selectedFileTitle != null && fselectedFile.textfile) {
                String fileContent = fileService.getTextFileContent(fselectedFile.getId());
                String fileName = fselectedFile.getFileTitle();

                // Ensure the filename has the .txt extension
                if (!fileName.endsWith(".txt")) {
                    fileName += ".txt";
                }

                StreamResource resource = new StreamResource(fileName, () -> new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8)));
                resource.setContentType("text/plain");


                Anchor downloadAnchor = new Anchor(resource, "Download");
                downloadAnchor.getElement().setAttribute("download", true);
                downloadAnchor.getElement().setAttribute("style", "display: none;");
                add(downloadAnchor);

                // Trigger download and then remove the anchor
                downloadAnchor.getElement().callJsFunction("click()");
            } else {
                Notification.show("No file selected to download.");
            }
        });
        downloadButton.addClassName("custom-button-black");

        deleteButton.addClickListener(e -> {
            if (selectedFileTitle != null) {
                deleteSelectedFile();
            } else {
                Notification.show("No file selected to delete.");
            }
        });


        saveButton.addClickListener(e -> {
            if (selectedFileTitle != null) {
                saveFileContent();
            } else {
                Notification.show("No file selected to save.");
            }
        });

        fileContentTextArea.setWidth("100%");
        fileContentTextArea.setHeight("120%");
        deleteButton.addClassName("custom-button-black");
        saveButton.addClassName("custom-button-black");
        /*addToWorkspaceButton.addClickListener(e -> {
            if (selectedFileTitle != null) {
                if(!fselectedFile.inPublicWorkspace)
                {
                    fselectedFile.inPublicWorkspace = true;
                    fileService.updateFileEntity(fselectedFile);
                    Notification.show("Added to Workspace");
                }
                else {
                    Notification.show("Already Added");
                }
            } else {
                Notification.show("No file selected to add to workspace.");
            }
        });*/

        VerticalLayout scratchpad = new VerticalLayout();
        scratchpad.add(upload, spText, fileContentTextArea, new HorizontalLayout(downloadButton, deleteButton, saveButton, addWorkSpace));
        fileContentTextArea.setWidthFull();

        VerticalLayout nt = new VerticalLayout();
        nt.add(usernameSpan, noteText, notesContainer);
        notesContainer.setWidthFull();
        noteText.setWidthFull();

        HorizontalLayout contentLayout = new HorizontalLayout(nt, scratchpad);
        contentLayout.setWidthFull();
        //contentLayout.setFlexGrow(0, grid);
        //contentLayout.setFlexGrow(0, fileContentTextArea);
        nt.setWidth("60%");
        scratchpad.setWidth("40%");
        //grid.setWidth("50%");

        add(contentLayout);
    }

    private HorizontalLayout createNotesLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        if(!fileEntities.isEmpty()) notesContainer.setText("");
        for(FileEntity fe : fileEntities){

            VerticalLayout temp = createUserTile(fe);
            temp.setWidth("200px");
            temp.addClickListener(event -> {
                String filetitle =  event.getSource().getElement().getAttribute("file-name");
                FileEntity selectedFile = fe;
                fselectedFile = selectedFile;

                Notification.show(filetitle + " has been selected");

                if (selectedFile != null) {
                    selectedFileTitle = selectedFile.getFileTitle();
                    if(selectedFile.textfile) {
                        fileContentTextArea.setVisible(true);
                        String fileContent = fileService.getTextFileContent(selectedFile.getId());
                        fileContentTextArea.setValue(fileContent);


                    }
                    else{
                        //Notification.show("Type not supported");
                        //fileContentTextArea.setVisible(false);
                        String fileUrl = UriComponentsBuilder.fromUriString("/filess")
                                .queryParam("title", fselectedFile.getFileTitle())
                                .toUriString();

                        Dialog dialog = getDialog(fileUrl);
                        dialog.open();
                    }
                } else {
                    selectedFileTitle = null;
                    fileContentTextArea.clear();
                    fileContentTextArea.setVisible(false);
                }
            });
            layout.add(temp);
        }
        return layout;
    }

    private Dialog getDialog(String fileUrl) {
        Anchor pdfAnchor = new Anchor(fileUrl, "Open PDF");
        pdfAnchor.setTarget("_blank");

        Button viewPdfButton = new Button("View PDF", event1 -> {
            getUI().ifPresent(ui -> ui.getPage().open(fileUrl, "_blank"));
        });

        Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();
        Text warning = new Text("Pdf will open in a new tab");
        Button close = new Button("Close");
        close.addClickListener(e-> dialog.close());

        dialogLayout.add(warning,viewPdfButton, close);
        dialog.add(dialogLayout);
        return dialog;
    }

    private VerticalLayout createUserTile(FileEntity user) {
        Image image = new Image("images/scrawl.webp", user.getUsername());
        image.addClassName("dash-image");

        Div title = new Div();
        title.setText(user.getFileTitle());
        title.addClassName("product-name");

        Div name = new Div();
        name.setText(user.getUsername() + " " + user.getUserId());
        name.addClassName("dash-name");

        Div description = new Div();
        description.setText(user.getUserInstitute());
        description.addClassName("dash-description");

        VerticalLayout v = new VerticalLayout();
        v.add(image, title, name, description);
        v.addClassName("product-tile");
        v.getElement().setAttribute("file-name", user.getFileTitle());

        return v;

       // add(headerLayout, upload, contentLayout, new HorizontalLayout(downloadButton, deleteButton, saveButton, addToWorkspaceButton));
    }

    private void refreshGrid() {
        grid.setItems(fileEntities);
    }

    private void deleteSelectedFile() {
//        FileEntity selectedFile = fileEntities.stream()
//                .filter(file -> file.getFileTitle().equals(selectedFileTitle))
//                .findFirst()
//                .orElse(null);
        if (fselectedFile != null) {
            fileEntities.remove(fselectedFile);
            fileService.deleteFileEntity(fselectedFile.getId());
            selectedFileTitle = null;
            fileContentTextArea.clear();

            notesContainer.removeAll();
            notesContainer.add(createNotesLayout());
            refreshGrid();
        }
        else Notification.show("no files selected to delete");
    }

    private void saveFileContent() {
        FileEntity selectedFile = fileEntities.stream()
                .filter(file -> file.getFileTitle().equals(selectedFileTitle))
                .findFirst()
                .orElse(null);
        if (selectedFile != null) {
            String newContent = fileContentTextArea.getValue();
            selectedFile.setFileContent(newContent.getBytes(Charset.forName("UTF-8")));
            fileService.saveFileEntity(selectedFile);
            Notification.show("Changes saved successfully", 2000, Notification.Position.MIDDLE);
        }
        else{
            if(fileContentTextArea.getValue() != null){
                String content = fileContentTextArea.getValue();
                String filename = "scratchpad.txt";

                /*try {
                    Path path = Paths.get(getBasePath() + filename);
                    Files.write(path, content.getBytes());
                    getUI().ifPresent(ui -> {
                        StreamResource resource = new StreamResource(filename, () -> {
                            try {
                                return Files.newInputStream(path);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        });
                        ui.getPage().open(resource, "_blank", false);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        }
    }
}
