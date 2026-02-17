package com.salle.sport.dto.abonnement_sports;

import jakarta.validation.constraints.NotNull;

public record DTO_post_abonnement_sports(
    @NotNull
    Long id_sports,
    @NotNull
    Long id_abonnement
){   
}
