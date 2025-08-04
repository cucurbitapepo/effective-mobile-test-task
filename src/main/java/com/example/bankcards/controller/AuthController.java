package com.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.bankcards.dto.request.JwtRequestDto;
import com.example.bankcards.dto.response.JwtResponseDto;
import com.example.bankcards.exception.handler.CustomErrorResponse;
import com.example.bankcards.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "API для аутентификации.")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "Эндпоинт для аутентификации.")
    @ApiResponse(
            responseCode = "200",
            description = "Токен аутентификации успешно создан",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = JwtResponseDto.class) // замените на реальный класс ответа
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Пользователь с таким id не найден",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class)
            )
    )
    @PostMapping("/auth")
    public ResponseEntity<?> createToken(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для аутентификации пользователя",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = JwtRequestDto.class)
                    )
            )
            @Validated @RequestBody JwtRequestDto requestDto) {
        return ResponseEntity.ok(authService.createAuthToken(requestDto));
    }
}
