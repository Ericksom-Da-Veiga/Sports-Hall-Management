package com.salle.sport.dto.abonnement;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record DTO_post_abonnement(
    @NotNull
    String cin,
    @NotNull
    LocalDate date_debut,
    @NotNull
    LocalDate date_fin,
    @NotNull
    Double prix_totale
) {
}