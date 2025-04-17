package com.example.application.view;

import com.example.application.entity.Utente;
import com.example.application.service.UtenteService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

public class UtenteForm extends VerticalLayout {

    public UtenteForm(UtenteService utenteService) {

        IntegerField idField = new IntegerField("ID");
        TextField nameField = new TextField("Nome");
        TextField surnameField = new TextField("Cognome");
        DatePicker birthDate = new DatePicker("Data di nascita");

        Button save = new Button("Salva", event -> {
            try {
                Utente utente = new Utente();
                utente.setId(idField.getValue());
                utente.setName(nameField.getValue());
                utente.setSurname(surnameField.getValue());
                utente.setBirth(birthDate.getValue());

                utenteService.insertOrUpdateUtente(utente);

                idField.clear();
                nameField.clear();
                surnameField.clear();
                birthDate.clear();

                Notification.show("Utente salvato", 3000, Notification.Position.MIDDLE);

            } catch (Exception e) {
                Notification.show("Errore: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        add(idField, nameField, surnameField, birthDate, save);
    }
}