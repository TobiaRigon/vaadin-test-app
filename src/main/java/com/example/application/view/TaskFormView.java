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

        TaskForm taskForm = new TaskForm();

        // 🔧 VERSIONE 1: form vuoto per compilazione manuale (DISATTIVATA) precompilato
        // per debug (ATTIVA)
        Task task = Task.builder()
                .name("")
                .description("")
                .status(Task.Status.DA_FARE)
                .priority(Task.Priority.ALTA)
                .scheduledDate(LocalDate.now().plusDays(1))
                .dueDate(LocalDate.now().plusDays(3))
                .creationDate(LocalDate.now())
                .build();

        // 🟩 Form con valori già compilati
        taskForm.setTask(task);

        /*
         * // 🔧 VERSIONE 2: precompilato per debug (ATTIVA)
         * Task task = Task.builder()
         * .name("Comprare il latte")
         * .description("Ricordarsi di prenderlo intero")
         * .status(Task.Status.DA_FARE)
         * .priority(Task.Priority.ALTA)
         * .scheduledDate(LocalDate.now().plusDays(1))
         * .dueDate(LocalDate.now().plusDays(3))
         * .creationDate(LocalDate.now())
         * .build();
         */

        taskForm.getSaveButton().addClickListener(e -> {
            Task formTask = taskForm.getCurrentTask();
            taskService.save(formTask);
            Notification.show("Task salvato: " + formTask.getName());

            // reset form – puoi scegliere se ricaricare vuoto o precompilato
            taskForm.setTask(Task.createNew());
        });

        add(taskForm);
        setSizeFull();
    }
}
