package com.example.application.view;

import com.example.application.entity.Pc;
import com.example.application.entity.Utente;
import com.example.application.service.PcService;
import com.example.application.service.UtenteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

import java.util.List;

public class UtenteForm extends VerticalLayout {

    private final UtenteService utenteService;
    private final PcService pcService;

    private final IntegerField idField = new IntegerField("ID");
    private final TextField nameField = new TextField("Nome");
    private final TextField surnameField = new TextField("Cognome");
    private final DatePicker birthDate = new DatePicker("Data di nascita");
    private final MultiSelectComboBox<Pc> pcSelector = new MultiSelectComboBox<>("Assegna PC");

    public UtenteForm(UtenteService utenteService, PcService pcService) {
        this.utenteService = utenteService;
        this.pcService = pcService;

        pcSelector.setItemLabelGenerator(Pc::getFullName);
        List<Pc> pcList = pcService.getAllPcs();
        pcSelector.setItems(pcList);

        Button save = new Button("Salva", event -> {
            try {
                Utente utente = new Utente();
                utente.setId(idField.getValue());
                utente.setName(nameField.getValue());
                utente.setSurname(surnameField.getValue());
                utente.setBirth(birthDate.getValue());

                utente.setPcList(pcSelector.getSelectedItems());
                utenteService.insertOrUpdateUtente(utente);

                idField.clear();
                nameField.clear();
                surnameField.clear();
                birthDate.clear();
                pcSelector.clear();

                Notification.show("Utente salvato", 3000, Notification.Position.MIDDLE);

            } catch (Exception e) {
                Notification.show("Errore: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        add(idField, nameField, surnameField, birthDate, pcSelector, save);
    }
}
