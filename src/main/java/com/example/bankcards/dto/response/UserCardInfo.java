package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с информацией по карте. Роль - Пользователь.")
public class UserCardInfo {
    @Schema(description = "Номер карты. Замаскирован.",
            example = "**** **** **** 1234")
    private String cardNumber;
    @Schema(description = "Дата истечения срока действия карты.")
    private LocalDate expirationDate;
    @Schema(description = "Статус карты.")
    private String cardStatus;
    @Schema(description = "Баланс карты.")
    private BigDecimal balance;
}
