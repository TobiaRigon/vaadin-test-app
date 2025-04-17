package com.example.application.dataProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.application.entity.Utente;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Integer> {
    // Custom query methods can be defined here if needed
    // For example, findByName(String name) or findBySurname(String surname)

}
