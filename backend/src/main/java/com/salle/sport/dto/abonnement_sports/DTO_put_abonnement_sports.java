package com.salle.sport.dto.abonnement_sports;

import jakarta.validation.constraints.NotNull;

public record DTO_put_abonnement_sports(
    Long id,
    Long id_sports,
    @NotNull
    Long id_abonnement
) {
} 