package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperForAdminTest {
    private final UserMapperForAdmin mapper = new UserMapperForAdminImpl();

    private String userId;
    private User user;
    private AdminUserInfoResponseDto adminUserInfoResponseDto;
    private UserInfoRequestDto userInfoRequestDto;

    @BeforeEach
    void setUp() {
        userId = "e487f331-5b7d-40b9-b49d-b41f94b2c960";
        user = User.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("John Doe")
                .username("username")
                .password("password")
                .role(UserRole.ROLE_USER)
                .build();
        adminUserInfoResponseDto = AdminUserInfoResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("John Doe")
                .role("ROLE_USER")
                .build();
        userInfoRequestDto = UserInfoRequestDto.builder()
                .firstAndLastName("John Doe")
                .username("username")
                .password("password")
                .role("ROLE_USER")
                .build();
    }

    @Test
    void entityToDto() {
        AdminUserInfoResponseDto actual = mapper.entityToDto(user);
        assertThat(actual)
                .isEqualTo(adminUserInfoResponseDto);
    }

    @Test
    void dtoToEntity() {
        user.setUserId(null);

        User actual = mapper.dtoToEntity(userInfoRequestDto);

        assertThat(actual)
                .isEqualTo(user);
    }
}