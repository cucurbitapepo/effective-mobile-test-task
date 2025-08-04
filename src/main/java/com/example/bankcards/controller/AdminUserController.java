package com.example.bankcards.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;
import com.example.bankcards.exception.handler.CustomErrorResponse;
import com.example.bankcards.service.AdminUserService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@Tag(name = "API для работы с пользователями. Роль - администратор.")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @Operation(summary = "Поиск пользователя по id.")
    @ApiResponse(
            responseCode = "200",
            description = "Информация о пользователе успешно получена",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminUserInfoResponseDto.class)
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
    @GetMapping("/{userId}")
    public ResponseEntity<AdminUserInfoResponseDto> getUserInformationByUserId(
            @Parameter(
                    description = "Уникальный идентификатор пользователя (UUID)",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable @UUID String userId) {
        return new ResponseEntity<>(adminUserService.getUserInformationById(userId), HttpStatus.OK);
    }

    @Operation(summary = "Получение информации по всем пользователям.")
    @ApiResponse(
            responseCode = "200",
            description = "Список всех пользователей успешно получен",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AdminUserInfoResponseDto.class))
            )
    )
    @GetMapping("/all")
    public ResponseEntity<List<AdminUserInfoResponseDto>> getAllUserInformation() {
        return new ResponseEntity<>(adminUserService.getAllUsers(), HttpStatus.OK);
    }

    @Operation(summary = "Создание нового пользователя.")
    @ApiResponse(
            responseCode = "201",
            description = "Пользователь успешно создан",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminUserInfoResponseDto.class)
            )
    )
    @PostMapping
    public ResponseEntity<AdminUserInfoResponseDto> createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для создания нового пользователя",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoRequestDto.class)
                    )
            )
            @Validated @RequestBody UserInfoRequestDto userInfoRequestDto) {
        return new ResponseEntity<>(adminUserService.createUser(userInfoRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "Изменение данных пользователя.")
    @ApiResponse(
            responseCode = "200",
            description = "Данные пользователя успешно обновлены",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AdminUserInfoResponseDto.class)
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
    @PutMapping("/{userId}")
    public ResponseEntity<AdminUserInfoResponseDto> updateUser(
            @Parameter(
                    name = "userId",
                    description = "Уникальный идентификатор пользователя (UUID)",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable @UUID String userId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для обновления пользователя",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserInfoRequestDto.class)
                    )
            )
            @Validated @RequestBody UserInfoRequestDto userInfoRequestDto) {
        return new ResponseEntity<>(adminUserService.updateUser(userId, userInfoRequestDto), HttpStatus.OK);
    }

    @Operation(summary = "Удаление пользователя.")
    @ApiResponse(
            responseCode = "200",
            description = "Пользователь успешно удалён",
            content = @Content(
                    mediaType = "text/plain",
                    schema = @Schema(type = "string", example = "Пользователь успешно удалён")
            )
    )
    @DeleteMapping(value = "/{userId}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity<String> deleteUser(
            @Parameter(
                    name = "userId",
                    description = "Уникальный идентификатор пользователя (UUID)",
                    required = true,
                    example = "123e4567-e89b-12d3-a456-426614174000"
            )
            @PathVariable @UUID String userId) {
        return new ResponseEntity<>(adminUserService.deleteUser(userId), HttpStatus.OK);
    }
}

