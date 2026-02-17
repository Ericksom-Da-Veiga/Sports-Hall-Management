package com.salle.sport.dto.message;

import jakarta.validation.constraints.NotNull;

public record DTO_put_message(
        @NotNull
        Long user1_id,
        @NotNull
        Long user2_id
) {
}
