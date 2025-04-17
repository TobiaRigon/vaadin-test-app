package com.example.application.view;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.entity.Utente;
import com.example.application.service.PcService;
import com.example.application.service.UtenteService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    @Autowired
    public MainView(UtenteService utenteService, PcService pcService) {
        UtenteForm utenteForm = new UtenteForm(utenteService, pcService);
        PcForm pcForm = new PcForm(pcService, utenteService);

        SplitLayout layout = new SplitLayout();
        layout.setSizeFull();
        layout.setSplitterPosition(50);

        layout.addToPrimary(utenteForm);
        layout.addToSecondary(pcForm);

        add(layout);
        setSizeFull();
    }
}
