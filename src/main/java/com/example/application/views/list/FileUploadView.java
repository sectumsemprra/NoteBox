package com.example.application.views.list;


import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AnonymousAllowed
@Route(value = "file", layout = MainLayout.class)
public class FileUploadView extends VerticalLayout {

    private Grid<String> grid = new Grid<>();
    private TextArea fileContentTextArea = new TextArea();

    private List<String> fileTitles = new ArrayList<>();
    private List<String> fileContents = new ArrayList<>();

    public FileUploadView() {
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setUploadButton(new Button("Upload Files"));


        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(buffer.getInputStream(fileName)));
                String contents = reader.lines().collect(Collectors.joining("\n"));
                fileTitles.add(fileName);
                fileContents.add(contents);

                //FileService.saveFile(fileName, contents);

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

        add(upload, contentLayout);
    }

    private void refreshGrid() {
        grid.setItems(fileTitles);
    }



}
