package com.example.bankcards.exception.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@Data
@Slf4j
@AllArgsConstructor
@Schema(description = "Структура ответа с ошибкой")
public class CustomErrorResponse {

    @Schema(description = "Время возникновения ошибки",
            example = "2025-05-04T21:00:00")
    private String timestamp;
    @Schema(description = "HTTP статус ошибки",
            example = "403")
    private int status;
    @Schema(description = "Сообщение об ошибке",
            example = "Операция невозможна - причина....")
    private String error;
    @Schema(description = "Эндпоинт, вызвавший ошибку",
            example = "/user/block/123e4567-e89b-12d3-a456-426614174000")
    private String path;

    public CustomErrorResponse(String message, HttpStatus status, WebRequest request) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status.value();
        this.error = message;
        this.path = ((ServletWebRequest) request).getRequest().getRequestURI();
        log.info("Принят ответ с ошибкой: отметка времени {} статус {} ошибка {} путь {}", timestamp, status, error, path);
    }
}
