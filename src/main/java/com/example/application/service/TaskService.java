package com.example.application.service;

import com.example.application.entity.Task;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();

    public TaskService() {
        System.out.println(">>> CREAZIONE TaskService");

        tasks.add(Task.builder()
                .id(UUID.randomUUID())
                .name("Comprare il latte")
                .description("Intero, senza lattosio se possibile")
                .status(Task.Status.DA_FARE)
                .priority(Task.Priority.ALTA)
                .creationDate(LocalDate.now())
                .scheduledDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(1))
                .build());

        tasks.add(Task.builder()
                .id(UUID.randomUUID())
                .name("Chiamare il commercialista")
                .description("Scadenza IVA trimestrale")
                .status(Task.Status.PROGRAMMATO)
                .priority(Task.Priority.MEDIA)
                .creationDate(LocalDate.now())
                .scheduledDate(LocalDate.now().plusDays(2))
                .dueDate(LocalDate.now().plusDays(5))
                .build());
    }

    public List<Task> findAll() {
        System.out.println("Trovo " + tasks.size() + " task");
        return new ArrayList<>(tasks);
    }

    public void save(Task task) {
        if (task.getId() == null) {
            task.setId(UUID.randomUUID());
            tasks.add(task);
        } else {
            tasks.replaceAll(t -> t.getId().equals(task.getId()) ? task : t);
        }
        System.out.println("Salvato: " + task.getName());
    }

    public void delete(Task task) {
        tasks.removeIf(t -> t.getId().equals(task.getId()));
        System.out.println("Eliminato: " + task.getName());
    }

}
