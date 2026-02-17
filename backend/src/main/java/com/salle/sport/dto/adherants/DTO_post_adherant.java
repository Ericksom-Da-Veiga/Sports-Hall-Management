package com.salle.sport.dto.adherants;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;

//dto pour recuperer et verifier les donnes reçu do front-end pour enregistrer les adherants
public record DTO_post_adherant(
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
    @Past
    LocalDate date_naissance,
    @NotBlank
    String adress,
    @NotBlank
    String telephone,
    @NotBlank
    String ville,
    @NotBlank
    String nom_pere,
    @NotBlank
    String nom_mere,
    @NotBlank
    String tel_parant
) {
    
}
