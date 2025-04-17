package com.example.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.application.dataProvider.PcRepository;
import com.example.application.entity.Pc;

@Service
public class PcService {

    @Autowired
    private PcRepository pcRepository; // Assuming you have a repository to handle database operations

    public List<Pc> getAllPcs() {
        return pcRepository.findAll();

    }

    public void insertOrUpdatePc(Pc pc) {
        pcRepository.save(pc); // Save the PC to the database

    }
}