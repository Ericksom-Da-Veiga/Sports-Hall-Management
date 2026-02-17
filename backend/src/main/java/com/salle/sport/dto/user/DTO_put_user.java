package com.salle.sport.dto.user;

import jakarta.validation.constraints.NotNull;

public record DTO_put_user(
    @NotNull
    Long id,
    String cin,
    String mail,
    String password,
    String nom, 
    String prenom,
    String telephone,
    String role
) {
}