package com.salle.sport.dto.abonnement_sports;

import com.salle.sport.entites.Abonnement_sports;

public record DTO_get_abonnement_sports(
    Long id,
    Long id_sports,
    Long id_abonnement
) {
    public DTO_get_abonnement_sports(Abonnement_sports data){
        this(
            data.getId(),
            data.getId_sports(),
            data.getId_abonnement()
        );
    }
}