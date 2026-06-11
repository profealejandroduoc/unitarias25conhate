package com.pruebas.unitarias25.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pruebas.unitarias25.model.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
}