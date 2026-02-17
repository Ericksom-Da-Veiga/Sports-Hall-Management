package com.salle.sport.dto.coach;

import java.time.LocalDate;

import com.salle.sport.entites.Coach;

public record DTO_list_coach(
    Long id, 
	String cin,
    String nom,
    String prenom,
    LocalDate date_entree,
    String telephone,
    String mail,
    String adress,
    String password,
    int idsport
) {

// Constructor pour crie un DTO_list_coach apartir d'un coach
    public DTO_list_coach(Coach coach){
        this(
            coach.getId(),
            coach.getCin(),
            coach.getNom(),
            coach.getPrenom(),
            coach.getDate_entree(),
            coach.getTelephone(),
            coach.getMail(),
            coach.getAdress(),
            coach.getPassword(),
            coach.getIdsport()
        );        
    }
}