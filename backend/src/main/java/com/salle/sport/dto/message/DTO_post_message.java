package com.salle.sport.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DTO_post_message(
        @NotNull
        Long senderId,
        @NotNull
        Long receiverId,
        @NotBlank
        String message
) {
}
