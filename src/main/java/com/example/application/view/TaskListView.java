package com.example.application.view;

import com.example.application.entity.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.HashMap;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vaadin.flow.spring.annotation.UIScope;

import jakarta.annotation.PostConstruct;
import com.vaadin.flow.component.checkbox.Checkbox;
import java.util.Map;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.BeforeEnterObserver;

import com.vaadin.flow.component.grid.ColumnTextAlign;

//@Component
@Route(value = "task-list", layout = MainLayout.class)
@PageTitle("Elenco Task")

public class TaskListView extends VerticalLayout implements BeforeEnterObserver {

    private final Map<UUID, Task.Status> originalStatuses = new HashMap<>();

    private final TextField titleFilter = new TextField();
    private final ComboBox<Task.Status> statusFilter = new ComboBox<>();
    private final ComboBox<Task.Priority> priorityFilter = new ComboBox<>();

    private final Grid<Task> grid = new Grid<>(Task.class, false);
    private final TaskService taskService;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
    }

    @Autowired
    public TaskListView(TaskService taskService) {
        this.taskService = taskService;
        setSizeFull();

        // Filtro per titolo
        titleFilter.setPlaceholder("Filtra per titolo");
        titleFilter.setClearButtonVisible(true);
        titleFilter.setValueChangeMode(ValueChangeMode.EAGER);

        // Filtro per stato
        statusFilter.setItems(Task.Status.values());
        statusFilter.setPlaceholder("Stato");
        statusFilter.setClearButtonVisible(true);

        // Filtro per priorità
        priorityFilter.setItems(Task.Priority.values());
        priorityFilter.setPlaceholder("Priorità");
        priorityFilter.setClearButtonVisible(true);

        // Pulsante "+"
        Button addButton = new Button(new Icon(VaadinIcon.PLUS));
        addButton.getElement().setAttribute("theme", "primary icon");
        addButton.addClickListener(e -> openAddDialog());

        // Layout filtri + bottone
        HorizontalLayout filterBar = new HorizontalLayout(titleFilter, statusFilter, priorityFilter, addButton);
        filterBar.setWidthFull();
        filterBar.setAlignItems(Alignment.END);
        add(filterBar);

        // Listener per filtrare
        titleFilter.addValueChangeListener(
                e -> refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue()));
        statusFilter.addValueChangeListener(
                e -> refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue()));
        priorityFilter.addValueChangeListener(
                e -> refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue()));

        // ✅ Griglia sotto
        configureGrid();
        refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());

        add(grid);
    }

    private void configureGrid() {

        grid.addComponentColumn(task -> {
            Checkbox checkbox = new Checkbox(task.getStatus() == Task.Status.FATTO);

            checkbox.addValueChangeListener(event -> {
                boolean checked = event.getValue();

                if (checked) {
                    originalStatuses.put(task.getId(), task.getStatus()); // salva stato precedente
                    task.setStatus(Task.Status.FATTO);
                } else {
                    Task.Status previous = originalStatuses.get(task.getId());
                    if (previous != null && previous != Task.Status.FATTO) {
                        task.setStatus(previous);
                    } else {
                        task.setStatus(Task.Status.DA_FARE); // fallback
                    }
                }

                taskService.save(task);
                refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
            });

            return checkbox;
        }).setHeader("✔")
                .setWidth("60px")
                .setFlexGrow(0)
                .setTextAlign(ColumnTextAlign.START);
        ;

        grid.addComponentColumn(task -> {
            Span title = new Span(task.getName());
            title.getElement().setProperty("title", task.getDescription());
            title.getStyle().set("cursor", "pointer");
            title.addClickListener(e -> openTitleDialog(task));
            return title;
        })
                .setComparator((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()))
                .setHeader("Titolo")
                .setSortable(true);

        // STATO - con badge e ordinamento
        grid.addComponentColumn(task -> {
            Span badge = new Span(task.getStatus().toString());
            badge.getElement().getThemeList().add("badge " + getStatusTheme(task.getStatus()));
            badge.getStyle().set("cursor", "pointer");
            badge.addClickListener(e -> openStatusDialog(task));
            return badge;
        })
                .setComparator(task -> task.getStatus().ordinal())
                .setHeader("Stato")
                .setSortable(true);

        // PRIORITÀ - con badge e ordinamento
        grid.addComponentColumn(task -> {
            Span badge = new Span(task.getPriority().toString());
            badge.getElement().getThemeList().add("badge " + getPriorityTheme(task.getPriority()));
            badge.getStyle().set("cursor", "pointer");
            badge.addClickListener(e -> openPriorityDialog(task));
            return badge;
        })
                .setComparator(task -> task.getPriority().ordinal())
                .setHeader("Priorità")
                .setSortable(true);

        grid.addComponentColumn(task -> {
            Span scheduledSpan = new Span(task.getScheduledDate() != null ? task.getScheduledDate().toString() : "—");
            scheduledSpan.getStyle().set("cursor", "pointer");
            scheduledSpan.addClickListener(e -> openScheduledDateDialog(task));
            return scheduledSpan;
        }).setHeader("Data programmata").setSortable(true);

        grid.addComponentColumn(task -> {
            Span dueDateSpan = new Span(task.getDueDate() != null ? task.getDueDate().toString() : "—");
            dueDateSpan.getStyle().set("cursor", "pointer");
            dueDateSpan.addClickListener(e -> openDueDateDialog(task));
            return dueDateSpan;
        }).setHeader("Scadenza").setSortable(true);

        grid.addComponentColumn(task -> {
            // HorizontalLayout actions = new HorizontalLayout();
            // actions.setSpacing(true);

            // Pulsante modifica
            // Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            // editButton.getElement().setAttribute("theme", "tertiary icon");
            // editButton.addClickListener(e -> openEditDialog(task));

            // Pulsante elimina
            Button deleteButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            deleteButton.getElement().setAttribute("theme", "error tertiary icon");
            deleteButton.addClickListener(e -> openDeleteDialog(task));

            // actions.add(deleteButton);
            return deleteButton;
        }).setHeader("Elimina")
                .setWidth("120px")
                .setFlexGrow(0)
                .setTextAlign(ColumnTextAlign.END);

        grid.setSizeFull();
    }

    private void openTitleDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Modifica Titolo e Descrizione");

        TextField nameField = new TextField("Titolo");
        nameField.setWidthFull();
        nameField.setValue(task.getName() != null ? task.getName() : "");

        com.vaadin.flow.component.textfield.TextArea descriptionArea = new com.vaadin.flow.component.textfield.TextArea(
                "Descrizione");
        descriptionArea.setWidthFull();
        descriptionArea.setHeight("150px");
        descriptionArea.setValue(task.getDescription() != null ? task.getDescription() : "");

        Button save = new Button("Salva", e -> {
            task.setName(nameField.getValue());
            task.setDescription(descriptionArea.getValue());
            taskService.save(task);
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        Button cancel = new Button("Annulla", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancel, save);
        buttons.setJustifyContentMode(JustifyContentMode.END);
        buttons.setWidthFull();

        VerticalLayout layout = new VerticalLayout(nameField, descriptionArea, buttons);
        layout.setPadding(false);
        layout.setSpacing(true);

        dialog.add(layout);
        dialog.setWidth("400px");
        dialog.open();
    }

    private void openStatusDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Modifica Stato");

        ComboBox<Task.Status> statusBox = new ComboBox<>("Seleziona stato");
        statusBox.setItems(Task.Status.values());
        statusBox.setValue(task.getStatus());

        Button save = new Button("Salva", e -> {
            task.setStatus(statusBox.getValue());
            taskService.save(task);
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        Button cancel = new Button("Annulla", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancel, save);
        dialog.add(statusBox, buttons);
        dialog.open();
    }

    private void openPriorityDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Modifica Priorità");

        ComboBox<Task.Priority> priorityBox = new ComboBox<>("Seleziona priorità");
        priorityBox.setItems(Task.Priority.values());
        priorityBox.setValue(task.getPriority());

        Button save = new Button("Salva", e -> {
            task.setPriority(priorityBox.getValue());
            taskService.save(task);
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        Button cancel = new Button("Annulla", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancel, save);
        dialog.add(priorityBox, buttons);
        dialog.open();
    }

    private void openScheduledDateDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Modifica Data Programmata");

        DatePicker datePicker = new DatePicker("Nuova data programmata");
        datePicker.setValue(task.getScheduledDate());

        Button save = new Button("Salva", e -> {
            task.setScheduledDate(datePicker.getValue());
            taskService.save(task);
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        Button cancel = new Button("Annulla", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancel, save);
        dialog.add(datePicker, buttons);
        dialog.open();
    }

    private void openDueDateDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Modifica Scadenza");

        DatePicker datePicker = new DatePicker("Nuova data di scadenza");
        datePicker.setValue(task.getDueDate());

        Button save = new Button("Salva", e -> {
            task.setDueDate(datePicker.getValue());
            taskService.save(task);
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        Button cancel = new Button("Annulla", e -> dialog.close());

        HorizontalLayout buttons = new HorizontalLayout(cancel, save);
        dialog.add(datePicker, buttons);
        dialog.open();
    }

    private void openDeleteDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Conferma eliminazione");

        dialog.add("Vuoi eliminare il task \"" + task.getName() + "\"?");

        Button confirm = new Button("Elimina", e -> {
            taskService.delete(task);
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        Button cancel = new Button("Annulla", e -> dialog.close());

        dialog.getFooter().add(cancel, confirm);
        dialog.open();
    }

    // private void openEditDialog(Task task) {
    // Dialog dialog = new Dialog();
    // dialog.setHeaderTitle("Modifica Task");

    // TaskFormEditor editor = new TaskFormEditor(taskService, () -> {
    // dialog.close();
    // refreshGrid(titleFilter.getValue(), statusFilter.getValue(),
    // priorityFilter.getValue());
    // });

    // editor.setTask(task); // Precarica i dati
    // dialog.add(editor);
    // dialog.open();
    // }

    private void openAddDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Nuovo Task");

        TaskFormEditor editor = new TaskFormEditor(taskService, () -> {
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        dialog.add(editor);
        dialog.open();
    }

    private void refreshGrid(String title, Task.Status status, Task.Priority priority) {
        grid.setItems(
                taskService.findAll().stream()
                        .filter(task -> title == null || title.isEmpty()
                                || task.getName().toLowerCase().contains(title.toLowerCase()))
                        .filter(task -> status == null || task.getStatus() == status)
                        .filter(task -> priority == null || task.getPriority() == priority)
                        .toList());
    }

    private String getStatusTheme(Task.Status status) {
        return switch (status) {
            case DA_FARE -> "primary";
            case IN_SVILUPPO -> "warning";
            case PROGRAMMATO -> "contrast";
            case STANDBY -> "tertiary";
            case FATTO -> "success";
        };
    }

    private String getPriorityTheme(Task.Priority priority) {
        return switch (priority) {
            case ALTA -> "error";
            case MEDIA -> "warning";
            case BASSA -> "success";
        };
    }

}
