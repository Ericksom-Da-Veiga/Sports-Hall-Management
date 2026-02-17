package com.salle.sport.dto.dashboard;

public record DTO_get_list_Dashboard(
    Long ActiveAbonnements,
    Long ActiveAdherants,
    Long TotalMoneyCollected
) {
}
