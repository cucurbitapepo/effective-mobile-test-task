package com.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.bankcards.dto.response.UserCardsResponseDto;
import com.example.bankcards.exception.handler.CustomErrorResponse;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "API для роли пользователя.")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Параметризованное получение списка карт пользователя.")
    @ApiResponse(
            responseCode = "200",
            description = "Список карт пользователя успешно получен",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserCardsResponseDto.class)
            )
    )
    @GetMapping("/all")
    public ResponseEntity<UserCardsResponseDto> getAllCardsInformation(
            @Parameter(description = "ID пользователя", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestHeader @UUID String userId,

            @Parameter(description = "Дата начала фильтрации по сроку действия карты", example = "2025-01-01")
            @RequestParam(required = false) LocalDate expireFrom,

            @Parameter(description = "Дата окончания фильтрации по сроку действия карты", example = "2025-12-31")
            @RequestParam(required = false) LocalDate expireTo,

            @Parameter(description = "Статус карты для фильтрации", example = "ACTIVE")
            @RequestParam(required = false) String cardStatus,

            @Parameter(description = "Номер страницы для пагинации", example = "1")
            @RequestParam(defaultValue = "1") int page,

            @Parameter(description = "Размер страницы для пагинации", example = "5")
            @RequestParam(defaultValue = "5") int pageSize) {
        return new ResponseEntity<>(userService.getAllCards(userId, expireFrom, expireTo, cardStatus, page, pageSize), HttpStatus.OK);
    }

    @Operation(summary = "Блокировка карты.")
    @ApiResponse(
            responseCode = "200",
            description = "Карта успешно заблокирована",
            content = @Content(
                    mediaType = "text/plain;charset=UTF-8",
                    schema = @Schema(type = "string", example = "Карта успешно заблокирована")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Карта не принадлежит пользователю.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class)
            )
    )
    @PutMapping(value = "/block/{cardId}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> blockCard(
            @Parameter(description = "ID пользователя", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestHeader @UUID String userId,

            @Parameter(description = "ID карты для блокировки", required = true, example = "d78a9c2f-f1d9-4e91-b26a-f84ef053c11a")
            @PathVariable @UUID String cardId) {
        return new ResponseEntity<>(userService.block(userId, cardId), HttpStatus.OK);
    }

    @Operation(summary = "Перевод средств межу своими картами.")
    @ApiResponse(
            responseCode = "200",
            description = "Перевод средств успешно выполнен",
            content = @Content(
                    mediaType = "text/plain;charset=UTF-8",
                    schema = @Schema(type = "string", example = "Перевод успешно выполнен")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Ошибка при переводе: карта не принадлежит пользователю или недостаточно средств",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class)
            )
    )
    @PutMapping(value = "/transfer", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> transferBetweenCards(
            @Parameter(description = "ID пользователя", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestHeader @UUID String userId,

            @Parameter(description = "ID карты-отправителя", required = true, example = "d78a9c2f-f1d9-4e91-b26a-f84ef053c11a")
            @RequestParam @UUID String idCardFrom,

            @Parameter(description = "ID карты-получателя", required = true, example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
            @RequestParam @UUID String idCardTo,

            @Parameter(description = "Сумма перевода", required = true, example = "1000.00")
            @RequestParam BigDecimal amount) {
        return new ResponseEntity<>(userService.transfer(userId, idCardFrom, idCardTo, amount), HttpStatus.OK);
    }

    @Operation(summary = "Получение баланса карты.")
    @ApiResponse(
            responseCode = "200",
            description = "Баланс карты успешно получен",
            content = @Content(
                    mediaType = "text/plain;charset=UTF-8",
                    schema = @Schema(type = "string", example = "15000.00")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Операция невозможна. Карта не принадлежит этому пользователю или другой сбой.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class)
            )
    )
    @GetMapping(value = "/balance/{cardId}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> getBalance(
            @Parameter(description = "ID пользователя", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @RequestHeader @UUID String userId,

            @Parameter(description = "ID карты", required = true, example = "d78a9c2f-f1d9-4e91-b26a-f84ef053c11a")
            @PathVariable @UUID String cardId) {
        return new ResponseEntity<>(userService.getBalance(userId, cardId), HttpStatus.OK);
    }
}
