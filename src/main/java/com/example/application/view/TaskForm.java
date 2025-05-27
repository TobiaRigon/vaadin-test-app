package com.example.application.view;

import com.example.application.entity.Task;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.StringLengthValidator;

import java.time.LocalDate;

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

        setResponsiveSteps(new ResponsiveStep("0", 1));
        add(name, description, status, priority, scheduledDate, dueDate, save);

        // VALIDAZIONI
        binder.forField(name)
                .asRequired("Il titolo è obbligatorio")
                .withValidator(new StringLengthValidator("Minimo 3 caratteri", 3, null))
                .bind(Task::getName, Task::setName);

        binder.forField(description)
                .withValidator(desc -> desc == null || desc.length() <= 200, "Massimo 200 caratteri")
                .bind(Task::getDescription, Task::setDescription);

        binder.forField(status)
                .asRequired("Seleziona uno stato")
                .bind(Task::getStatus, Task::setStatus);

        binder.forField(priority)
                .asRequired("Seleziona una priorità")
                .bind(Task::getPriority, Task::setPriority);

        binder.forField(scheduledDate)
                // .asRequired("Data programmata obbligatoria")
                .bind(Task::getScheduledDate, Task::setScheduledDate);

        binder.forField(dueDate)
                // .asRequired("Data scadenza obbligatoria")
                .withValidator(due -> {
                    LocalDate sched = scheduledDate.getValue();
                    return sched == null || !due.isBefore(sched);
                }, "La scadenza non può essere prima della data programmata")
                .bind(Task::getDueDate, Task::setDueDate);
    }

    public void setTask(Task task) {
        binder.setBean(task);
    }

    public Button getSaveButton() {
        return save;
    }

    public Task getCurrentTask() {
        Task bean = binder.getBean();
        try {
            binder.writeBean(bean);
        } catch (ValidationException e) {
            e.printStackTrace();
            return null;
        }
        return bean;
    }
}
