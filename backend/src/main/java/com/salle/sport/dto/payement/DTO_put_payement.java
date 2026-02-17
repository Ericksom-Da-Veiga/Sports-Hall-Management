package com.salle.sport.dto.payement;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record DTO_put_payement(
    @NotNull
    Long id,
    LocalDate date_payement,
    Long id_abonnement,
    Long quant_recu
){
}
