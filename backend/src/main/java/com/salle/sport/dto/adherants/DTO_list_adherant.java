package com.salle.sport.dto.adherants;

import java.time.LocalDate;

import com.salle.sport.entites.Adherant;

//DTO pour lister le/les adherants
public record DTO_list_adherant(
    Long id,
    String cin,
    String mail,
    String nom,
    String prenom,
    LocalDate date_naissance,
    String adress,
    String telephone,
    String ville,
    String nom_pere,
    String nom_mere,
    String tel_parant,
    int active
){
// constructor que nous permet de prend a Adherant et lister ses informations
    public DTO_list_adherant(Adherant adherant){
        this(
            adherant.getId(),
            adherant.getCin(),
            adherant.getMail(),
            adherant.getNom(),
            adherant.getPrenom(),
            adherant.getDate_naissance(),
            adherant.getAdress(),
            adherant.getTelephone(),
            adherant.getVille(),
            adherant.getNom_pere(),
            adherant.getNom_mere(),
            adherant.getTel_parant(),
            adherant.getActive()
        );
    }

}
