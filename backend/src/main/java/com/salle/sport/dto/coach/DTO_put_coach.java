package com.salle.sport.dto.coach;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record DTO_put_coach(
    @NotNull
    Long id,
    String cin,
    String mail,
    String password,
    String nom,
    String prenom,
    @Past
    LocalDate date_entree,
    String adress,
    String telephone,
    int idsport
) {
    
}
