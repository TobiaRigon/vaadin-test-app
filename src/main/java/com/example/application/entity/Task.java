package com.example.application.entity;

import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private UUID id;
    private String name;
    private String description;

    private Status status;
    private Priority priority;

    private LocalDate creationDate;
    private LocalDate scheduledDate;
    private LocalDate dueDate;

    public enum Status {
        DA_FARE, PROGRAMMATO, STANDBY, FATTO
    }

    public enum Priority {
        ALTA, MEDIA, BASSA
    }

    public static Task createNew() {
        return Task.builder()
                .id(UUID.randomUUID())
                .name("") // <- mai null
                .description("")
                .status(Status.DA_FARE)
                .priority(Priority.MEDIA)
                .creationDate(LocalDate.now())
                .scheduledDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(3))
                .build();
    }

}
