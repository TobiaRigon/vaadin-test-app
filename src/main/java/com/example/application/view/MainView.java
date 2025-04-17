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

    public MainView(UtenteService utenteService, PcService pcService) {
        SplitLayout layout = new SplitLayout();

        layout.setSizeFull();
        layout.setSplitterPosition(50); // 50%/50%

        // Componenti modulari
        UtenteForm utenteForm = new UtenteForm(utenteService);
        PcForm pcForm = new PcForm(pcService);

        layout.addToPrimary(utenteForm);
        layout.addToSecondary(pcForm);

        add(layout);
        setSizeFull(); // occupa tutto lo spazio
    }
}
