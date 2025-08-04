package com.example.bankcards.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.example.bankcards.entity.enums.UserRole;
import com.example.bankcards.util.annotations.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO запроса на создание или изменение данных пользователя. Роль - Администратор.")
public class UserInfoRequestDto {
    @Schema(description = "Имя и фамилия(если нужно) пользователя",
            example = "Dmitriy Ivanov")
    @NotBlank(message = "Фамилия и имя пользователя не могут быть пустыми")
    @Length(max = 50)
    private String firstAndLastName;

    @Schema(description = "Username(login) пользователя",
            example = "username123")
    @NotBlank(message = "Username не может быть пустым")
    @Length(max = 50)
    private String username;

    @Schema(description = "Пароль пользователя",
            example = "userpass321")
    @NotBlank(message = "Пароль не может быть пустым")
    @Length(max = 100)
    private String password;

    @Schema(description = "Роль пользователя(ограничение прав доступа). Может быть только ROLE_USER или ROLE_ADMIN",
            example = "ROLE_USER")
    @NotNull(message = "Роль пользователя не может быть пустым")
    @EnumValue(enumClass = UserRole.class, message = "Роль пользователя может быть только ROLE_USER или ROLE_ADMIN")
    private String role;
}
