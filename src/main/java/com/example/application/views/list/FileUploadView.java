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
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

        Span usernameSpan = new Span("Logged in as: " + username);
        usernameSpan.getStyle().set("margin-left", "auto");
        finalUsername = username;
        Userr userr = authService.findByUsername(finalUsername);
        finalUserId = userr.getId();
        fileContentTextArea.setVisible(false);

        fileContentTextArea.getStyle().set("overflow", "auto");
        fileContentTextArea.getStyle().set("max-height", "400px");

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.add(usernameSpan);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload Files"));
        upload.setAcceptedFileTypes("application/pdf", "text/plain");
        upload.getElement().executeJs("this.shadowRoot.querySelector('vaadin-upload-file').style.display = 'none';");

        List<FileEntity> existingFiles = fileService.getFileEntityByUsername(username);
        existingFiles.forEach(file -> {
            if(file.inDashboard) {
                fileEntities.add(file);
            }
        });
        refreshGrid();

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
                refreshGrid();
                upload.getElement().executeJs("this.files = []");


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        grid.addColumn(FileEntity::getFileTitle).setHeader("Uploaded Files");
        grid.addColumn(FileEntity::getUploadDate).setHeader("Upload Date");

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
        });

//
//        Anchor downloadAnchor = new Anchor(resource, "");
//        downloadAnchor.getElement().setAttribute("download", true);
//        downloadAnchor.getElement().setAttribute("style", "display: none;");
//
//
//        Button downloadButton = new Button("Download", event -> {
//            downloadAnchor.getElement().callJsFunction("click");
//        });

        downloadButton.addClickListener(e -> {
            if (selectedFileTitle != null) {
                deleteSelectedFile();
            } else {
                Notification.show("No file selected to delete.");
            }
        });



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

        addToWorkspaceButton.addClickListener(e -> {
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

        HorizontalLayout contentLayout = new HorizontalLayout(grid, fileContentTextArea);
        contentLayout.setWidthFull();
        contentLayout.setFlexGrow(0, grid);
        contentLayout.setFlexGrow(1, fileContentTextArea);
        contentLayout.setWidth("100%");
        grid.setWidth("50%");
        fileContentTextArea.setWidth("50%");

        add(headerLayout, upload, contentLayout, new HorizontalLayout(downloadButton, deleteButton, saveButton, addToWorkspaceButton));
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
            refreshGrid();
        }
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
    }
}
