package com.example.application.views;

import com.example.application.security.SecurityService;
import com.example.application.views.list.FileUploadView;
import com.example.application.views.list.ListView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

public class AdminLayout extends AppLayout {

    private SecurityService securityService;
    public AdminLayout(SecurityService securityService)
    {
        this.securityService = securityService;
        createHeader();
        createDrawer();
    }
    private void createHeader() {
        H1 logo = new H1("NoteBox");
        logo.addClassName("logo");

        Button logoutbtn = new Button("Log out", e -> LogoutView.logout());
        logoutbtn.addClassName("custom-button-white");

        Button settingsButton = new Button("Settings");
        settingsButton.addClassName("custom-button-white");
        settingsButton.addClickListener(e -> UI.getCurrent().navigate("/settings"));

        HorizontalLayout header = new HorizontalLayout(
                new DrawerToggle(),
                logo,
                settingsButton,
                logoutbtn
        );

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidth("100%");
        header.addClassNames("header");

        addToNavbar(header);

    }

    private void createDrawer() {
        RouterLink listLink = new RouterLink("Public Workspace", ListView.class);
        RouterLink uploadData = new RouterLink("Upload", FileUploadView.class);
        RouterLink dashboardlink = new RouterLink("Dashboard", DashboardView.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        listLink.setClassName("link");
        dashboardlink.setClassName("link");
        uploadData.setClassName("link");

        addToDrawer(new VerticalLayout(
                listLink,
                dashboardlink,
                uploadData
        ));

    }


}
