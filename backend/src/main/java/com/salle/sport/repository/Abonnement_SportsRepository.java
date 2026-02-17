package com.salle.sport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salle.sport.entites.Abonnement_sports;

public interface Abonnement_SportsRepository extends JpaRepository<Abonnement_sports, Long> {

    @Query("select a from Abonnement_sports a where a.id_abonnement = :id_abonnement ")
    List<Abonnement_sports> findAllByIdAbonnement(Long id_abonnement);

    // @Modifying
    // @Transactional
    // @Query("INSERT INTO AbonnementSports (idAbonnement, idSports) VALUES (:idAbonnement, :idSports)")
    // void sauvegarder(Long idAbonnement, Long idSports);
    
}
