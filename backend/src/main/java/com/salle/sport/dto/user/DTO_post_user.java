package com.salle.sport.dto.user;

import jakarta.validation.constraints.NotBlank;

public record DTO_post_user(
    @NotBlank
    String cin,
    @NotBlank
    String mail,
    @NotBlank
    String password,
    @NotBlank
    String nom, 
    @NotBlank
    String prenom,
    @NotBlank
    String telephone,
    @NotBlank
    String role
) {
}