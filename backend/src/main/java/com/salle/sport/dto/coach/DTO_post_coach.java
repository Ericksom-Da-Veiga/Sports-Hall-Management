package com.salle.sport.dto.coach;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DTO_post_coach(
    @NotBlank //pour eviter que l'utilisateur saisi une String vide
    String cin,
    @NotBlank
    String mail,
    @NotBlank
    String password,
    @NotBlank
    String nom,
    @NotBlank
    String prenom,
    LocalDate date_entree,
    @NotBlank
    String adress,
    @NotBlank
    String telephone,
    @NotNull
    int idsport
){
}
