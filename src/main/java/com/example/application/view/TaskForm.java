package com.example.application.view;

import com.example.application.entity.Task;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class TaskForm extends FormLayout {

    private final TextField name = new TextField("Titolo");
    private final TextArea description = new TextArea("Descrizione");
    private final ComboBox<Task.Status> status = new ComboBox<>("Stato");
    private final ComboBox<Task.Priority> priority = new ComboBox<>("Priorità");
    private final DatePicker scheduledDate = new DatePicker("Data programmata");
    private final DatePicker dueDate = new DatePicker("Scadenza");

    private final Button save = new Button("Salva");
    private final Binder<Task> binder = new Binder<>(Task.class);

    public TaskForm() {
        status.setItems(Task.Status.values());
        priority.setItems(Task.Priority.values());

        setResponsiveSteps(
                new ResponsiveStep("0", 1));

        add(name, description, status, priority, scheduledDate, dueDate, save);

        // 🔧 Collega tutti i campi al binder (usa i nomi delle proprietà di Task)
        binder.bindInstanceFields(this);
    }

    // ✅ Corretta
    public void setTask(Task task) {
        binder.setBean(task);
    }

    public Button getSaveButton() {
        return save;
    }

    public Task getCurrentTask() {
        Task bean = binder.getBean();
        try {
            binder.writeBean(bean); // <-- questo scrive i dati del form dentro l'oggetto
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

}
