package com.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.bankcards.dto.request.CardInfoRequestDto;
import com.example.bankcards.dto.response.AdminCardInfoResponseDto;
import com.example.bankcards.exception.handler.CustomErrorResponse;
import com.example.bankcards.service.AdminCardService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/cards")
@Tag(name = "API для работы с картами. Роль - администратор.")
public class AdminCardController {
    private final AdminCardService adminCardService;

    @Operation(summary = "Поиск карты по id.")
    @ApiResponse(
            responseCode = "200",
            description = "Информация о карте успешно получена",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminCardInfoResponseDto.class)
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Карта с таким id не найдена в базе данных",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class)
            )
    )
    @GetMapping("/{cardId}")
    public ResponseEntity<AdminCardInfoResponseDto> getCardInformationByCardId(
            @Parameter(
                    name = "cardId",
                    description = "Уникальный идентификатор карты (UUID)",
                    required = true,
                    example = "d78a9c2f-f1d9-4e91-b26a-f84ef053c11a"
            )
            @PathVariable @UUID String cardId) {
        return new ResponseEntity<>(adminCardService.getCardInformationById(cardId), HttpStatus.OK);
    }

    @Operation(summary = "Поиск всех карт.")
    @ApiResponse(
            responseCode = "200",
            description = "Список всех карт успешно получен",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AdminCardInfoResponseDto.class))
            )
    )
    @GetMapping("/all")
    public ResponseEntity<List<AdminCardInfoResponseDto>> getAllCardsInformation() {
        return new ResponseEntity<>(adminCardService.getAllCards(), HttpStatus.OK);
    }

    @Operation(summary = "Создание карт.")
    @ApiResponse(
            responseCode = "201",
            description = "Карта успешно создана",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminCardInfoResponseDto.class)
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
    @PostMapping
    public ResponseEntity<AdminCardInfoResponseDto> createCard(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания новой карты",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CardInfoRequestDto.class)
                    )
            )
            @Validated @RequestBody CardInfoRequestDto cardInfoRequestDto) {
        return new ResponseEntity<>(adminCardService.createCard(cardInfoRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение статуса карты.")
    @ApiResponse(
            responseCode = "200",
            description = "Статус карты успешно изменён",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Статус успешно обновлён")
            )
    )
    @ApiResponse(
            responseCode = "404",
            description = "Карта с таким id не найдена в базе данных",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CustomErrorResponse.class)
            )
    )
    @PutMapping(value = "/", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> changeStatus(
            @Parameter(
                    name = "cardId",
                    description = "Уникальный идентификатор карты (UUID)",
                    required = true,
                    example = "d78a9c2f-f1d9-4e91-b26a-f84ef053c11a"
            )
            @RequestParam @UUID String cardId,

            @Parameter(
                    name = "status",
                    description = "Новый статус карты",
                    required = true,
                    example = "ACTIVE"
            )
            @RequestParam String status) {
        return new ResponseEntity<>(adminCardService.changeStatus(cardId, status), HttpStatus.OK);
    }

    @Operation(summary = "Удаление карты.")
    @ApiResponse(
            responseCode = "200",
            description = "Карта успешно удалена",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Карта успешно удалена")
            )
    )
    @DeleteMapping(value = "/{cardId}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> deleteCard(
            @Parameter(
                    name = "cardId",
                    description = "Уникальный идентификатор карты (UUID)",
                    required = true,
                    example = "d78a9c2f-f1d9-4e91-b26a-f84ef053c11a"
            )
            @PathVariable @UUID String cardId) {
        return new ResponseEntity<>(adminCardService.delete(cardId), HttpStatus.OK);
    }
}
