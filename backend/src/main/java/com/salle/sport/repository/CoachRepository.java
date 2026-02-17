package com.salle.sport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salle.sport.entites.Coach;

import jakarta.validation.constraints.NotBlank;

public interface CoachRepository extends JpaRepository<Coach, Long> {
    @Query(value = "CALL findCoachByCinOrNom(?1)", nativeQuery = true)
    List<Coach> findCoachByCinOrNom(String data);

    @Query("select c from Coach c where c.cin = :cin")
    Coach findByCin(@NotBlank String cin);

    @Query("select c from Coach c where c.mail = :mail")
    Object findByMail(@NotBlank String mail);

    // @Query(value = "SELECT id, cin, mail, password, c.nom, c.prenom, c.date_entree, c.adress, c.telephone FROM coach c WHERE c.cin LIKE :data OR c.nom LIKE :data OR c.prenom LIKE :data", nativeQuery = true)
    // List<Coach> findCoachByCinOrNom(@Param("data") String data);
} 

