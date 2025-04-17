package com.example.application.entity;

import java.time.LocalDate;

import org.checkerframework.checker.units.qual.g;

import com.vaadin.flow.component.template.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Utente {

    @jakarta.persistence.Id
    @Column(name = "id")
    @NotNull(message = "Id cannot be null")
    private Integer id;

    @Column(name = "name")
    @Size(min = 2, max = 25, message = "Name must be between 2 and 25 characters")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birth")
    @Past(message = "Birth date must be in the past")
    private LocalDate birth;

    public String getFullName() {
        return name + " " + surname;
    }

}