package com.example.application.dataProvider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.application.entity.Pc;

@Repository
public interface PcRepository extends JpaRepository<Pc, Integer> {
    // Custom query methods can be defined here if needed
    // For example, findByName(String name) or findByBrand(String brand)

}
