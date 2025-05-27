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
import java.util.HashMap;

import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.router.BeforeEnterObserver;

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
        }).setHeader("✔");

        grid.addComponentColumn(task -> {
            Span title = new Span(task.getName());
            title.getElement().setProperty("title", task.getDescription());
            return title;
        })
                .setComparator((t1, t2) -> t1.getName().compareToIgnoreCase(t2.getName()))
                // <-- fix critico!
                .setHeader("Titolo")
                .setSortable(true);

        // STATO - con badge e ordinamento
        grid.addComponentColumn(task -> {
            Span badge = new Span(task.getStatus().toString());
            badge.getElement().getThemeList().add("badge " + getStatusTheme(task.getStatus()));
            return badge;
        })
                .setComparator(task -> task.getStatus().ordinal()) // ordina per enum ordinal
                .setHeader("Stato")
                .setSortable(true);

        // PRIORITÀ - con badge e ordinamento
        grid.addComponentColumn(task -> {
            Span badge = new Span(task.getPriority().toString());
            badge.getElement().getThemeList().add("badge " + getPriorityTheme(task.getPriority()));
            return badge;
        })
                .setComparator(task -> task.getPriority().ordinal()) // ordina per enum ordinal
                .setHeader("Priorità")
                .setSortable(true);

        grid.addColumn(Task::getScheduledDate).setHeader("Data programmata").setSortable(true);
        grid.addColumn(Task::getDueDate).setHeader("Scadenza").setSortable(true);

        grid.addComponentColumn(task -> {
            HorizontalLayout actions = new HorizontalLayout();
            actions.setSpacing(true);

            // Pulsante modifica
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.getElement().setAttribute("theme", "tertiary icon");
            editButton.addClickListener(e -> openEditDialog(task));

            // Pulsante elimina
            Button deleteButton = new Button(new Icon(VaadinIcon.CLOSE_SMALL));
            deleteButton.getElement().setAttribute("theme", "error tertiary icon");
            deleteButton.addClickListener(e -> openDeleteDialog(task));

            actions.add(editButton, deleteButton);
            return actions;
        }).setHeader("Azioni");

        grid.setSizeFull();
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

    private void openEditDialog(Task task) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Modifica Task");

        TaskFormEditor editor = new TaskFormEditor(taskService, () -> {
            dialog.close();
            refreshGrid(titleFilter.getValue(), statusFilter.getValue(), priorityFilter.getValue());
        });

        editor.setTask(task); // Precarica i dati
        dialog.add(editor);
        dialog.open();
    }

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
