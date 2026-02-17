package com.salle.sport.dto.abonnement;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record DTO_put_abonnement(
    @NotNull
    Long id,
    Long id_adherant,
    LocalDate date_debut,
    LocalDate date_fin,
    Double prix_totale
) {

}