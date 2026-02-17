package com.salle.sport.dto.user;

import com.salle.sport.entites.Users;

public record DTO_get_user(
    Long id,
    String cin,
    String mail,
    String nom, 
    String prenom,
    String telephone,
    String role
) {
    public DTO_get_user(Users users) {
        this(
            users.getId(),
            users.getCin(),
            users.getMail(),
            users.getNom(),
            users.getPrenom(),
            users.getTelephone(),
            users.getRole()
        );
    }
}