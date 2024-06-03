package com.example.application.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("/")
@AnonymousAllowed
@CssImport("./Login-view.css")
public class MainView extends VerticalLayout {

    public MainView() {
        addClassName("mainbg");
        //Image img = new Image("")
        createHeader();
        createHeroSection();
        createContentSection();
        createFooter();
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
        Div heroSection = new Div();
        heroSection.addClassName("hero-section");

        VerticalLayout textLayout = new VerticalLayout();

        H1 headline = new H1("Share Your Notes, Boost Your Knowledge!");
        headline.addClassName("hero-text");
        Span subHeadline = new Span("Welcome to NoteBox, a platform designed to revolutionize the way you learn and collaborate. Whether you're a student aiming for better grades or a professional seeking to stay updated, our app empowers you to learn more effectively through diverse perspectives and organized content. Join our community and experience a smarter, more collaborative way to enhance your knowledge!");
        subHeadline.addClassName("span-text");
        Button exploreButton = new Button("START YOUR JOURNEY NOW!");
        exploreButton.addClassName("custom-button-black");
        exploreButton.addClickListener(e -> UI.getCurrent().navigate("/login"));

        textLayout.add(headline, subHeadline, exploreButton);
        textLayout.setAlignItems(Alignment.CENTER);
        heroSection.add(textLayout);
        heroSection.setWidthFull();
        add(heroSection);
    }

    private void createContentSection() {
        HorizontalLayout contentSection = new HorizontalLayout();
        contentSection.addClassName("content-section");

        // Placeholder content
        VerticalLayout content1 = createContentBox("Collaborative Learning", "Users can contribute their notes, creating a rich repository of information.\n" +
                "Collaboration can lead to a deeper understanding of topics through different perspectives.", "READ MORE");
        VerticalLayout content2 = createContentBox("Revision and Study Aid", "Users can use shared notes to review and prepare for exams, ensuring they cover all key points and topics.", "READ MORE");
        VerticalLayout content3 = createContentBox("Time-Saving", "Accessing pre-made notes can save users significant time, allowing them to focus on understanding and revising rather than creating notes from scratch.", "READ MORE");

        contentSection.add(content1, content2, content3);

        add(contentSection);
    }

    private VerticalLayout createContentBox(String title, String description, String buttonText) {
        VerticalLayout contentBox = new VerticalLayout();
        contentBox.addClassName("content-box");

        H1 contentTitle = new H1(title);
        contentTitle.addClassName("hero-text-h1");

        Span contentDescription = new Span(description);
        contentDescription.addClassName("hero-text-span");
        Button contentButton = new Button(buttonText);
        contentButton.addClassName("custom-button-black");
        contentButton.addClickListener(e->UI.getCurrent().navigate("/aboutus"));

        contentBox.add(contentTitle, contentDescription, contentButton);
        contentBox.setAlignItems(Alignment.CENTER);
        return contentBox;
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
        foot.setAlignItems(Alignment.CENTER);

        container.add(foot);
        container.setWidthFull();
        // Add container to the footer
        add(container);
    }
}

