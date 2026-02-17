package com.salle.sport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salle.sport.entites.Abonnement;


public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    
    @Query(value = "CALL findAbonnementByCinOrNom(?1)", nativeQuery = true)
    List<Abonnement> findByCinOrNom(String data);

    @Query(value = "CALL findAbonnementByCinOnly(?1)", nativeQuery = true)
    Abonnement findByCinOnly(String cin);

    @Query(value = "CALL findActiveAbonnements()", nativeQuery = true)
    List<Abonnement> findActiveAbonnements();

    @Query(value = "CALL findAbonnementById(?1)", nativeQuery = true)
    List<Abonnement> findAbonnementById(Long id);

    @Query("Select count(a) from Abonnement a where a.active = 1")
    Long countAbonnement();

    @Query(value = "CALL findAbonnementPresqueFini()", nativeQuery = true)
    List<Abonnement> findAbonnementPresqueFini();
}