package com.example.bankcards.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO с JWT и id пользователя. Предполагаем что id будет подшиваться в header запросов от пользователя на фронте")
public class JwtResponseDto {
    @Schema(description = "id пользователя.")
    private String userId;
    @Schema(description = "JWT для сессий.")
    private String token;
}
