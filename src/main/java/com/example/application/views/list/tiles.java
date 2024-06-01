package com.example.application.views.list;

import com.example.application.data.Contact;
import com.example.application.entity.FileEntity;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class tiles extends VerticalLayout {
    FileEntity fileEntity;
    public tiles( FileEntity fileEntity){
        //this.contact = ct;
        Image image = new Image("images/images.png", "contact image");
        image.addClassName("product-image");

        Div name = new Div();
        name.setText(fileEntity.getFileTitle());
        name.addClassName("product-name");

        Div description = new Div();
        description.setText(fileEntity.getUsername());
        description.addClassName("product-description");

        add(image, name, description);
    }

}
