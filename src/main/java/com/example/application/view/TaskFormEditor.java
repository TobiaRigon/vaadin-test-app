package com.example.application.view;

import com.example.application.entity.Task;
import com.example.application.service.TaskService;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TaskFormEditor extends VerticalLayout {

    private final TaskForm form;
    private final TaskService taskService;
    private final Runnable onSaveCallback;

    public TaskFormEditor(TaskService taskService, Runnable onSaveCallback) {
        this.taskService = taskService;
        this.onSaveCallback = onSaveCallback;

        form = new TaskForm();
        add(form);

        setupNewTask();

        form.getSaveButton().addClickListener(e -> {
            Task current = form.getCurrentTask();
            taskService.save(current);
            Notification.show("Task salvato: " + current.getName());

            // 🔄 resetta il form con un nuovo Task vuoto
            setupNewTask();

            // 🔁 esegue la callback (es. chiudi dialog, aggiorna lista)
            onSaveCallback.run();
        });
    }

    private void setupNewTask() {
        Task newTask = Task.builder()
                .name("")
                .description("")
                .status(Task.Status.DA_FARE)
                .priority(Task.Priority.MEDIA)
                .creationDate(java.time.LocalDate.now())
                .scheduledDate(null)
                .dueDate(null)
                .build();

        form.setTask(newTask);
    }

    public void setTask(Task task) {
        form.setTask(task);
    }
}
