package com.example.application.view;

import com.example.application.entity.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@Component
@Route(value = "task-list", layout = MainLayout.class)
@PageTitle("Elenco Task")
public class TaskListView extends VerticalLayout {

    private final Grid<Task> grid = new Grid<>(Task.class, false);

    @Autowired
    public TaskListView(TaskService taskService) {
        setSizeFull();
        configureGrid();

        // carica task dalla memoria
        grid.setItems(taskService.findAll());

        add(grid);
    }

    private void configureGrid() {
        grid.addColumn(Task::getName).setHeader("Titolo");
        grid.addColumn(Task::getStatus).setHeader("Stato");
        grid.addColumn(Task::getPriority).setHeader("Priorità");
        grid.addColumn(Task::getScheduledDate).setHeader("Data programmata");
        grid.addColumn(Task::getDueDate).setHeader("Scadenza");

        grid.setSizeFull();
    }
}
