package com.example.application.view;

import java.time.LocalDate;

import com.example.application.entity.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Nuovo Task")
public class TaskFormView extends VerticalLayout {

    public TaskFormView(TaskService taskService) {
        TaskFormEditor editor = new TaskFormEditor(taskService, () -> {
        });
        add(editor);
        setSizeFull();
    }
}
