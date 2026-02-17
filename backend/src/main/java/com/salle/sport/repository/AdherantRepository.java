package com.salle.sport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salle.sport.entites.Adherant;

import jakarta.validation.constraints.NotNull;

public interface AdherantRepository extends JpaRepository<Adherant, Long> {

    @Query(value = "CALL findActiveAdherants()", nativeQuery = true)
    List<Adherant> findActiveAdherants();

    @Query(value = "CALL findAdherantByCinOrNom(?1)", nativeQuery = true)
    List<Adherant> findByCinOrNom(String nom);

    @Query("select a from Adherant a where a.cin = :cin")
    Adherant findByCin(@NotNull String cin);

    @Query("select a from Adherant a where a.mail = :mail")
    Adherant findByMail(@NotNull String mail);

    @Query("Select count(a) from Adherant a where a.active = 1")
    Long countAdherants();

}