package com.salle.sport.dto.sports;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DTO_post_sports(
    @NotBlank
    String nom,
    @NotNull
    Long nmbr_max_seance_semaine,
    @NotNull
    Double prix
) {
    
}
