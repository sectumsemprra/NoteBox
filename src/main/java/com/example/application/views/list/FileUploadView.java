package com.example.application.views.list;

import com.example.application.data.Userr;
import com.example.application.entity.FileEntity;
import com.example.application.service.FileService;
import com.example.application.services.AuthService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private final Button saveButton = new Button("Save Changes");
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

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.add(usernameSpan);

        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload Files"));

        List<FileEntity> existingFiles = fileService.getFileEntityByUsername(username);
        existingFiles.forEach(file -> {
            if(file.inDashboard) {
                fileEntities.add(file);
            }
        });
        refreshGrid();

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(buffer.getInputStream(fileName)));
                String contents = reader.lines().collect(Collectors.joining("\n"));
                String us = finalUsername;

                FileEntity fileEntity = new FileEntity(finalUserId, fileName, contents, us);
                fileEntity.inDashboard = true;
                fileEntity.setUploadDate(LocalDateTime.now());
                fileEntity.setUserInstitute(userr.getInstitute());
                fileService.saveFileEntity(fileEntity);
                fileEntities.add(fileEntity);
                refreshGrid();

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
                fileContentTextArea.setValue(selectedFile.getFileContent());
            } else {
                selectedFileTitle = null;
                fileContentTextArea.clear();
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
               fselectedFile.inPublicWorkspace = true;
               fileService.updateFileEntity(fselectedFile);
                Notification.show("Added to Workspace");
            } else {
                Notification.show("No file selected to add to workspace.");
            }
        });

        HorizontalLayout contentLayout = new HorizontalLayout(grid, fileContentTextArea);
        contentLayout.setWidthFull();
        contentLayout.setFlexGrow(0, grid);
        contentLayout.setFlexGrow(1, fileContentTextArea);
        contentLayout.setWidth("100%");
        grid.setWidth("40%");
        fileContentTextArea.setWidth("60%");

        add(headerLayout, upload, contentLayout, new HorizontalLayout(deleteButton, saveButton, addToWorkspaceButton));
    }

    private void refreshGrid() {
        grid.setItems(fileEntities);
    }

    private void deleteSelectedFile() {
        FileEntity selectedFile = fileEntities.stream()
                .filter(file -> file.getFileTitle().equals(selectedFileTitle))
                .findFirst()
                .orElse(null);
        if (selectedFile != null) {
            fileEntities.remove(selectedFile);
            fileService.deleteFileEntity(selectedFile.getId());
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
            selectedFile.setFileContent(newContent);
            fileService.saveFileEntity(selectedFile);
            Notification.show("Changes saved successfully", 2000, Notification.Position.MIDDLE);
        }
    }
}
