package com.salle.sport.dto.payement;

import java.time.LocalDate;

import com.salle.sport.entites.Payement;

public record DTO_get_payements(
    Long id,
    LocalDate date_payement,
    Long id_abonnement,
    Long quant_recu,
    String cin_adherant,
    long rendu
) {
    public DTO_get_payements(Payement data){
        this(
            data.getId(),
            data.getDate_payement(),
            data.getId_abonnement(),
            data.getQuant_recu(),
            data.getCin_adherant(),
            data.getRendu()
        );
    }
}