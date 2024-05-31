package com.example.application.views.list;

import com.example.application.data.Contact;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class tiles extends VerticalLayout {
    Contact contact;
    public tiles(Contact ct){
        //this.contact = ct;
        Image image = new Image("images/images.png", "contact image");
        image.addClassName("product-image");

        Div name = new Div();
        name.setText(ct.getFirstName());
        name.addClassName("product-name");

        Div description = new Div();
        description.setText(ct.getCompany().getName());
        description.addClassName("product-description");

        add(image, name, description);
    }

}
