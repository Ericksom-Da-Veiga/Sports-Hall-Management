package com.salle.sport.dto.payement;

import java.time.LocalDate;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DTO_post_payements(
    @NotNull
    LocalDate date_payement,
    @NotNull
    Long id_abonnement,
    @NotNull
    Long quant_recu,
    @NotEmpty
    String cin_adherant,
    @NotNull
    Long rendu
) {
    
}
