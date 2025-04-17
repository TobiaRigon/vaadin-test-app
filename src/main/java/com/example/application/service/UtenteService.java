package com.example.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.dataProvider.UtenteRepository;
import com.example.application.entity.Utente;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    public void insertOrUpdateUtente(Utente utente) {
        utenteRepository.save(utente);
    }

}
