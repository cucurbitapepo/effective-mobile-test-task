package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.util.annotations.EnumValue;
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
@Schema(description = "DTO запроса на создание новой карты. Роль - Администратор.")
public class CardInfoRequestDto {
    @Schema(description = "Номер карты. Должен иметь вид 16 цифр в блоках по 4 разделенных пробелом",
            example = "1111 2222 3333 4444")
    @NotBlank(message = "Поле cardNumber не должно быть пустым")
    @Pattern(regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$",
            message = "Номер карты должен выглядеть как 16 цифр в блоках по 4 разделенных пробелом")
    private String cardNumber;

    @Schema(description = "id пользователя. Формат UUID",
            example = "4f23a559-5689-4c82-a79c-9fe064db4455")
    @NotNull(message = "Введите корректный UUID")
    private String userId;

    @Schema(description = "Дата окончания срока действия карты",
            example = "2028-12-31")
    @NotNull(message = "Дата окончания действия карты обязательна")
    @FutureOrPresent(message = "Дата окончания действия карты должна быть в будущем или сегодня")
    private LocalDate expirationDate;

    @Schema(description = "Статус карты. Может быть только ACTIVE, BLOCKED или EXPIRED",
            example = "ACTIVE")
    @NotNull(message = "Статус карты обязателен")
    @EnumValue(enumClass = CardStatus.class, message = "Статус может быть только ACTIVE, BLOCKED или EXPIRED")
    private String cardStatus;

    @Schema(description = "Баланс карты. Предполагаем, что при создании карты минуса не может быть",
            example = "22.3")
    @DecimalMin(value = "0.0", message = "Баланс не может быть отрицательным")
    private BigDecimal balance;
}
