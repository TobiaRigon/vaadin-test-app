package com.example.application.view;

import com.example.application.entity.Pc;
import com.example.application.service.PcService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

public class PcForm extends VerticalLayout {

    public PcForm(PcService pcService) {

        IntegerField idField = new IntegerField("ID");
        TextField marcaField = new TextField("Marca");
        TextField modelloField = new TextField("Modello");
        DatePicker dataAcquisto = new DatePicker("Data Acquisto");

        Button save = new Button("Salva", event -> {
            try {
                Pc pc = new Pc();
                pc.setId(idField.getValue());
                pc.setName(marcaField.getValue());
                pc.setBrand(modelloField.getValue());
                pc.setPurchaseDate(dataAcquisto.getValue());

                pcService.insertOrUpdatePc(pc);

                idField.clear();
                marcaField.clear();
                modelloField.clear();
                dataAcquisto.clear();

                Notification.show("PC salvato", 3000, Notification.Position.MIDDLE);

            } catch (Exception e) {
                Notification.show("Errore: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        add(idField, marcaField, modelloField, dataAcquisto, save);
    }
}