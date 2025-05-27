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

    public TaskService() {
        System.out.println(">>> CREAZIONE TaskService");
    }

}
