package com.example.application.init;

import com.example.application.entity.Pc;
import com.example.application.entity.Utente;
import com.example.application.service.PcService;
import com.example.application.service.UtenteService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UtenteService utenteService;
    private final PcService pcService;

    public DataInitializer(UtenteService utenteService, PcService pcService) {
        this.utenteService = utenteService;
        this.pcService = pcService;
    }

    @Override
    public void run(String... args) {
        if (utenteService.getAllUtenti().isEmpty() && pcService.getAllPcs().isEmpty()) {
            // Creazione PC
            Pc pc1 = new Pc(null, "Dell", "XPS", LocalDate.of(2021, 4, 10), new HashSet<>());
            Pc pc2 = new Pc(null, "HP", "Envy", LocalDate.of(2022, 1, 22), new HashSet<>());

            // Salvataggio PC
            pcService.insertOrUpdatePc(pc1);
            pcService.insertOrUpdatePc(pc2);

            // Creazione Utenti
            Utente u1 = new Utente(null, "Mario", "Rossi", LocalDate.of(1990, 1, 1), new HashSet<>());
            Utente u2 = new Utente(null, "Laura", "Bianchi", LocalDate.of(1988, 6, 15), new HashSet<>());

            // Associazione
            u1.getPcList().add(pc1);
            u2.getPcList().add(pc1);
            u2.getPcList().add(pc2);

            // Salvataggio Utenti (owner della relazione)
            utenteService.insertOrUpdateUtente(u1);
            utenteService.insertOrUpdateUtente(u2);

            System.out.println("Dati di test inseriti correttamente.");
        }
    }
}
