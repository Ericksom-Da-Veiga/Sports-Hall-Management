package com.salle.sport.dto.adherants;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

public record DTO_put_adherant(
    @NotNull
    Long id,
    String cin,
    String mail,
    String password,
    String nom,
    String prenom,
    @Past
    LocalDate date_naissance,
    String adress,
    String telephone,
    String ville,
    String nom_pere,
    String nom_mere,
    String tel_parant
) {
    
}
