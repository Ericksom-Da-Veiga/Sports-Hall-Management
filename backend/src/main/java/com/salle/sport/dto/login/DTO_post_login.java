package com.salle.sport.dto.login;

import jakarta.validation.constraints.NotBlank;

public record DTO_post_login(
    @NotBlank
    String mail,
    @NotBlank
    String password
) {
    
}
