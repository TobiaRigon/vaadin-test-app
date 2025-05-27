package com.example.application.view;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Task Tracker");
        logo.getStyle().set("font-size", "var(--lumo-font-size-l)").set("margin", "0");

        HorizontalLayout header = new HorizontalLayout(logo);
        header.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        header.setWidthFull();
        header.getStyle().set("padding", "1rem");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink formLink = new RouterLink("Inserisci Task", TaskFormView.class);
        RouterLink listLink = new RouterLink("Elenco Task", TaskListView.class);

        addToDrawer(formLink, listLink);
    }
}
