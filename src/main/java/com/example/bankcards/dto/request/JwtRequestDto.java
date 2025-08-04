package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO запроса на получение JWT.")
public class JwtRequestDto {
    @Schema(description = "Username(login) пользователя",
            example = "username123")
    @NotBlank(message = "Поле username не должно быть пустым")
    private String username;

    @Schema(description = "Пароль пользователя",
            example = "userpass321")
    @NotBlank(message = "Поле password не должно быть пустым")
    private String password;
}
