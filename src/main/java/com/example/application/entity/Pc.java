package com.example.application.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pc {

    @Id
    @Column(name = "id")
    @NotNull(message = "Id cannot be null")
    private Integer id;

    @Column(name = "name")
    @Size(min = 2, max = 25, message = "Name must be between 2 and 25 characters")
    private String name;

    @Column(name = "brand")
    @Size(min = 2, max = 25, message = "Brand must be between 2 and 25 characters")
    private String brand;

    @Column(name = "PurchaseDate")
    @NotNull(message = "Purchase date cannot be null")
    private LocalDate purchaseDate;

    public String getFullName() {
        return name + " " + brand;
    }

    /**
     * Un PC può essere assegnato a più utenti
     * mappedBy = "pcList" perché è il nome del campo nella classe Utente
     */
    @ManyToMany(mappedBy = "pcList")
    private Set<Utente> utenteList = new HashSet<>();

}
