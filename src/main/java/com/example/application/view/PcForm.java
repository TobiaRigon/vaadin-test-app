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
import java.util.Set;

public class PcForm extends VerticalLayout {

    private final PcService pcService;
    private final UtenteService utenteService;

    private final MultiSelectComboBox<Utente> utenteSelector = new MultiSelectComboBox<>("Assegna utenti");

    public PcForm(PcService pcService, UtenteService utenteService) {
        this.pcService = pcService;
        this.utenteService = utenteService;

        IntegerField idField = new IntegerField("ID");
        TextField marcaField = new TextField("Marca");
        TextField modelloField = new TextField("Modello");
        DatePicker dataAcquisto = new DatePicker("Data Acquisto");

        utenteSelector.setItemLabelGenerator(Utente::getFullName);
        List<Utente> utenti = utenteService.getAllUtenti();
        utenteSelector.setItems(utenti);

        Button save = new Button("Salva", event -> {
            try {
                Pc pc = new Pc();
                pc.setId(idField.getValue());
                pc.setName(marcaField.getValue());
                pc.setBrand(modelloField.getValue());
                pc.setPurchaseDate(dataAcquisto.getValue());

                Set<Utente> utentiSelezionati = utenteSelector.getSelectedItems();
                for (Utente u : utentiSelezionati) {
                    u.getPcList().add(pc);
                    utenteService.insertOrUpdateUtente(u);
                }

                pcService.insertOrUpdatePc(pc);

                idField.clear();
                marcaField.clear();
                modelloField.clear();
                dataAcquisto.clear();
                utenteSelector.clear();

                Notification.show("PC salvato", 3000, Notification.Position.MIDDLE);

            } catch (Exception e) {
                Notification.show("Errore: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        add(idField, marcaField, modelloField, dataAcquisto, utenteSelector, save);
    }
}
