package com.example.application.views.list;

import com.example.application.data.Userr;
import com.example.application.entity.FileEntity;
import com.example.application.service.FileService;
import com.example.application.services.AuthService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletService;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.SpringComponent;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.application.services.AuthService.getCurrentUsername;

@AnonymousAllowed
@Route(value = "file", layout = MainLayout.class)

public class FileUploadView extends VerticalLayout {

    private final Grid<String> grid = new Grid<>();
    private final TextArea fileContentTextArea = new TextArea();
    private final Button deleteButton = new Button("Delete Selected File");
    private final Button saveButton = new Button("Save Changes");

    private final List<String> fileTitles = new ArrayList<>();
    private final List<String> fileContents = new ArrayList<>();
    private final AuthService authService;
    private final FileService fileService;
    private String selectedFileTitle;
    private final String  finalUsername;
    private final int  finalUserid;




    public FileUploadView(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;

        System.out.println("FIle upload created");
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

        // Create a Span to display the username
        Span usernameSpan = new Span("Logged in as: " + username);
        usernameSpan.getStyle().set("margin-left", "auto");
        finalUsername = username;
        Userr userr = authService.findByUsername(finalUsername);
        finalUserid = userr.getId();

        // Create the layout for the header
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.add(usernameSpan);

        // File upload setup
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload Files"));

        List<FileEntity> existingFiles = fileService.getFileEntityByUsername(username); // Implement this method in FileService
        existingFiles.forEach(file -> {
            if(file.inDashboard) {
                fileTitles.add(file.getFileTitle());
                fileContents.add(file.getFileContent());
            }

        });
        refreshGrid();

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(buffer.getInputStream(fileName)));
                String contents = reader.lines().collect(Collectors.joining("\n"));
                String us = finalUsername;

                fileTitles.add(fileName);
                fileContents.add(contents);
                FileEntity fileEntity = new FileEntity(finalUserid, fileName, contents, us);
                fileEntity.inDashboard = true;
                fileService.saveFileEntity(fileEntity);

            } catch (Exception e) {
                e.printStackTrace();
            }
            refreshGrid();
        });

        grid.addColumn(String::toString).setHeader("Uploaded Files");
        grid.addSelectionListener(event -> {
            String selectedTitle = event.getFirstSelectedItem().orElse(null);
            if (selectedTitle != null) {
                selectedFileTitle = selectedTitle;
                int index = fileTitles.indexOf(selectedTitle);
                if (index >= 0 && index < fileContents.size()) {
                    fileContentTextArea.setValue(fileContents.get(index));
                }
            } else {
                selectedFileTitle = null;
                fileContentTextArea.clear();
            }
        });

        deleteButton.addClickListener(e -> {
            if (selectedFileTitle != null) {
                deleteSelectedFile();
            } else {
                System.out.println("No file selected to delete.");
            }
        });

        saveButton.addClickListener(e -> {
            if (selectedFileTitle != null) {
                saveFileContent();
            } else {
                System.out.println("No file selected to save.");
            }
        });

        HorizontalLayout contentLayout = new HorizontalLayout(grid, fileContentTextArea);
        contentLayout.setWidthFull(); // Make the layout fill the available width

        // Set the width of each component within the layout
        contentLayout.setFlexGrow(0, grid); // Prevent the grid from expanding
        contentLayout.setFlexGrow(1, fileContentTextArea); // Allow the text area to expand

        // Set the width of each component explicitly
        contentLayout.setWidth("100%"); // Make the layout fill the available width
        grid.setWidth("40%");
        fileContentTextArea.setWidth("60%");

        // Add the header layout and other components to the view
        add(headerLayout, upload, contentLayout, new HorizontalLayout(deleteButton, saveButton));
    }

    private void refreshGrid() {
        grid.setItems(fileTitles);
    }

    private void deleteSelectedFile() {
        int index = fileTitles.indexOf(selectedFileTitle);
        if (index >= 0) {
            fileTitles.remove(index);
            fileContents.remove(index);
            FileEntity fileEntity = fileService.getFileEntityByTitle(selectedFileTitle);
            fileService.deleteFileEntity(fileEntity.getId());
            selectedFileTitle = null;
            fileContentTextArea.clear();
            refreshGrid();
        }
    }

    private void saveFileContent() {
        int index = fileTitles.indexOf(selectedFileTitle);
        if (index >= 0) {
            String newContent = fileContentTextArea.getValue();
            fileContents.set(index, newContent);
            FileEntity fileEntity = fileService.getFileEntityByTitle(selectedFileTitle);
            fileEntity.setFileContent(newContent);
            fileService.saveFileEntity(fileEntity);
            Notification.show("Changes saved successfully", 2000, Notification.Position.MIDDLE);
        }
    }
}