package com.example.application.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.checkerframework.checker.units.qual.g;

import com.vaadin.flow.component.template.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    // @NotNull(message = "Id cannot be null")
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

    /**
     * Un utente può avere più PC
     * Relazione molti-a-molti con la tabella di relazione "utente_pc"
     */
    @ManyToMany
    @JoinTable(name = "utente_pc", // nome tabella di join
            joinColumns = @JoinColumn(name = "utente_id"), // FK verso la tabella utenti
            inverseJoinColumns = @JoinColumn(name = "pc_id") // FK verso la tabella pc
    )
    private Set<Pc> pcList = new HashSet<>();

}