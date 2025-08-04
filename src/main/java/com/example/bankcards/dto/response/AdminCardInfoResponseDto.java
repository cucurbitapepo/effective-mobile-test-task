package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с информацией по карте. Роль - Администратор.")
public class AdminCardInfoResponseDto {
    @Schema(description = "id карты")
    private UUID cardId;
    @Schema(description = "Номер карты - не замаскирован. Предполагаем что администратор видит всё.")
    private String cardNumber;
    @Schema(description = "Фамилия и имя пользователя")
    private String firstAndLastName;
    @Schema(description = "Дата истечения срока карты")
    private LocalDate expirationDate;
    @Schema(description = "Статус карты")
    private String cardStatus;
    @Schema(description = "Баланс карты")
    private BigDecimal balance;
}
