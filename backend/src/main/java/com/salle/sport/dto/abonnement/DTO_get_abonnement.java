package com.salle.sport.dto.abonnement;

import java.time.LocalDate;

import com.salle.sport.entites.Abonnement;


public record DTO_get_abonnement(
    Long id,
    Long id_adherant,
    LocalDate date_debut,
    LocalDate date_fin,
    Double prix_totale,
    int active
) {

    public DTO_get_abonnement(Abonnement abonnement) {
        this(
            abonnement.getId(),
            abonnement.getId_adherant(),
            abonnement.getDate_debut(),
            abonnement.getDate_fin(),
            abonnement.getPrix_totale(),
            abonnement.getActive()
        );
    };
}