package com.example.application.views.list;

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
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.application.services.AuthService.getCurrentUsername;

@AnonymousAllowed
@Route(value = "file", layout = MainLayout.class)
@SpringComponent
public class FileUploadView extends VerticalLayout {

    private final Grid<String> grid = new Grid<>();
    private final TextArea fileContentTextArea = new TextArea();

    private final List<String> fileTitles = new ArrayList<>();
    private final List<String> fileContents = new ArrayList<>();
    private final AuthService authService;
    private final FileService fileService;

    @Autowired
    public FileUploadView(AuthService authService, FileService fileService) {
        this.authService = authService;
        this.fileService = fileService;

        // Retrieve the current username
        String username = getCurrentUsername();

        // Create a Span to display the username
        Span usernameSpan = new Span("Logged in as: " + username);
        usernameSpan.getStyle().set("margin-left", "auto");

        // Create the layout for the header
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setWidthFull();
        headerLayout.add(usernameSpan);

        // File upload setup
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload Files"));

        List<FileEntity> existingFiles = fileService.getFileEntities(); // Implement this method in FileService
        existingFiles.forEach(file -> {
            fileTitles.add(file.getFileTitle());
            fileContents.add(file.getFileContent());
        });
        refreshGrid();

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(buffer.getInputStream(fileName)));
                String contents = reader.lines().collect(Collectors.joining("\n"));
                fileTitles.add(fileName);
                fileContents.add(contents);
                FileEntity fileEntity = new FileEntity(1, fileName, contents);
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
                int index = fileTitles.indexOf(selectedTitle);
                if (index >= 0 && index < fileContents.size()) {
                    fileContentTextArea.setValue(fileContents.get(index));
                }
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
        add(headerLayout, upload, contentLayout);
    }

    private void refreshGrid() {
        grid.setItems(fileTitles);
    }
}
