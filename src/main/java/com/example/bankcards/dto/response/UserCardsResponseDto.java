package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с информацией по всем картам пользователя. Роль - Пользователь.")
public class UserCardsResponseDto {
    @Schema(description = "Количество страниц(для фронта).")
    private Integer totalPages;
    @Schema(description = "DTO с информацией по карте. Роль - Пользователь. Список карт")
    private List<UserCardInfo> cards;
}
