package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO с информацией о пользователе. Роль - Администратор. Предполагаем что логин и пароль администратору не нужны.")
public class AdminUserInfoResponseDto {
    @Schema(description = "id пользователя")
    private UUID userId;
    @Schema(description = "Фамилия и имя пользователя")
    private String firstAndLastName;
    @Schema(description = "Роль пользователя.")
    private String role;
}
