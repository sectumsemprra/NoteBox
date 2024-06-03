package com.example.application.views;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("/aboutus")
@AnonymousAllowed
public class aboutUs extends VerticalLayout{
    public aboutUs() {
        addClassName("mainbg");
        //Image img = new Image("")
        createHeader();
        createHeroSection();
        createContentSection();
        createTeamSection();
        createFooter();
        setSpacing(false);
    }

    private void createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");

        H1 logo = new H1("NoteBox");
        logo.addClassName("logo");

        HorizontalLayout navBar = new HorizontalLayout();
        navBar.addClassName("nav-bar");

        Button login = new Button("Log In");
        login.addClassName("custom-button-white");
        login.addClickListener(e -> UI.getCurrent().navigate("/login"));
        Button signup = new Button("Sign Up");
        signup.addClassName("custom-button-white");
        signup.addClickListener(e -> UI.getCurrent().navigate("/register"));

        navBar.add(login, signup);
        header.add(logo, navBar);
        header.setWidthFull();
        add(header);
    }

    private void createHeroSection() {
        Div header = new Div();
        header.addClassName("rmbg");
        header.addClassName("container");


        H1 headline = new H1("About Us");
        headline.addClassName("rm-text");

        header.add(headline);
        header.setWidthFull();
        header.setHeight("200px");
        add(header);
    }

    private void createContentSection() {
        Div heroSection = new Div();
        heroSection.addClassName("almost-hero");

        VerticalLayout textLayout = new VerticalLayout();

        H1 headline = new H1(" \"Sharing is the essence of teaching. It is, I have come to believe, the essence of civilization.\" — Bill Moyers");
        headline.addClassName("ah-text");
        Span subHeadline = new Span("The idea for our notes sharing app was born out of a simple realization: the power of collaboration and shared knowledge. As students and professionals, we often found ourselves overwhelmed by the sheer volume of information we needed to manage. We noticed that when we collaborated and shared our notes, we not only lightened our individual workloads but also enriched our understanding through diverse perspectives.\n" +
                "\n" +
                "Motivated by this insight, we set out to create an intuitive platform where users could effortlessly share, access, and collaborate on notes. Our goal was to foster a community where learning and knowledge-sharing were seamless and accessible to all. After countless brainstorming sessions, user feedback, and iterations, we proudly launched our notes sharing app, dedicated to enhancing the way we learn and grow together.");
        subHeadline.addClassName("ah-span-text");

        textLayout.add(headline, subHeadline);
        textLayout.setAlignItems(Alignment.CENTER);
        heroSection.add(textLayout);
        heroSection.setWidthFull();
        add(heroSection);
    }

    private void createTeamSection(){
        Div mainn = new Div();
        mainn.addClassName("teambg");

        VerticalLayout textLayout = new VerticalLayout();

        H2 headline = new H2("Meet The Team");
        headline.addClassName("ah-text");

        VerticalLayout mem1 = new VerticalLayout();
        mem1.add(CustomCard("images/duck2.webp", "Mehreen Hossain Chowdhury", "loves to sleep"));

        VerticalLayout mem2 = new VerticalLayout();
        mem2.add(CustomCard("images/duck1.webp", "Sumaiya Ahmed Rani", "loves to sleep 2"));

        VerticalLayout mem3 = new VerticalLayout();
        mem3.add(CustomCard("images/duck3.jpg", "Jannatul Fardus Rakhi", "loves to sleep 3"));

        HorizontalLayout members = new HorizontalLayout();
        members.add(mem1, mem2, mem3);
        members.setAlignItems(Alignment.CENTER);
        members.setHeight("100%");

        Button exploreButton = new Button("Back To Homepage");
        exploreButton.addClassName("custom-button-black");
        exploreButton.addClickListener(e -> UI.getCurrent().navigate("/"));
        exploreButton.getStyle().set("padding", "10px");

        textLayout.add(headline, members, exploreButton);
        textLayout.setAlignItems(Alignment.CENTER);
        mainn.add(textLayout);
        mainn.setWidthFull();
        add(mainn);

    }

    public VerticalLayout CustomCard(String imageUrl, String name, String title) {
        // Create the image component
        VerticalLayout card = new VerticalLayout();
        Image image = new Image(imageUrl, "Profile Image");
        image.addClassName("profile-image");

        // Create a container for the image
        Div imageContainer = new Div(image);
        imageContainer.addClassName("imagee-container");

        // Create the name and title components
        Html nameHtml = new Html("<div class='card-name'>" + name + "</div>");
        Html titleHtml = new Html("<div class='card-title'>" + title + "</div>");

        // Create a container for the text
        Div textContainer = new Div();
        textContainer.addClassName("text-containerr");
        textContainer.add(nameHtml, titleHtml);

        // Create a main container to hold both the image and the text
        Div mainContainer = new Div(imageContainer, textContainer);
        mainContainer.addClassName("custom-card");

        // Add components to the layout
        card.add(mainContainer);


        return card;
    }

    private void createFooter(){
        Div container = new Div();
        container.addClassName("footer");

        VerticalLayout foot = new VerticalLayout();
        // Content of the footer
        Span text = new Span("© 2024 Notebox. All rights reserved.");
        text.addClassName("footer-text");
        Anchor privacyLink = new Anchor("#", "Privacy Policy");
        privacyLink.setClassName("footer-text");
        Anchor termsLink = new Anchor("#", "Terms of Service");
        termsLink.setClassName("footer-text");

        // Add components to the container
        foot.add(text, privacyLink, termsLink);
        foot.setAlignItems(FlexComponent.Alignment.CENTER);

        container.add(foot);
        container.setWidthFull();
        // Add container to the footer
        add(container);
    }
}
