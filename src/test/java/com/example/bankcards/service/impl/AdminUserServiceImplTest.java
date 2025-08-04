package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mapper.UserMapperForAdmin;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapperForAdmin mapper;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private User user;
    private User user2;
    private String userId;
    private AdminUserInfoResponseDto adminUserInfoResponseDto;
    private AdminUserInfoResponseDto adminUserInfoResponseDto2;
    private UserInfoRequestDto userInfoRequestDto;

    @BeforeEach
    void setUp() {
        userId = "e487f331-5b7d-40b9-b49d-b41f94b2c960";
        user = User.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("John Doe")
                .build();
        user2 = User.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("Mihail Nekrasov")
                .build();
        adminUserInfoResponseDto = AdminUserInfoResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("John Doe")
                .build();
        adminUserInfoResponseDto2 = AdminUserInfoResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("Mihail Nekrasov")
                .build();
        userInfoRequestDto = UserInfoRequestDto.builder()
                .firstAndLastName("John Doe")
                .build();
    }

    @Nested
    class GetUserById {
        @Test
        void getUserInformationByIdWhenPresent() {
            when(userRepository.findById(any()))
                    .thenReturn(Optional.of(user));
            when(mapper.entityToDto(user))
                    .thenReturn(adminUserInfoResponseDto);

            AdminUserInfoResponseDto actual = adminUserService.getUserInformationById(userId);

            assertThat(actual)
                    .isEqualTo(adminUserInfoResponseDto);
        }

        @Test
        void getUserInformationByIdWhenNotPresent() {
            when(userRepository.findById(any()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminUserService.getUserInformationById(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("Пользователь с таким id не найден в базе данных");
        }
    }

    @Nested
    class GetAllUsers {
        @Test
        void getAllUsers() {
            when(userRepository.findAll())
                    .thenReturn(List.of(user, user2));
            when(mapper.entityToDto(user))
                    .thenReturn(adminUserInfoResponseDto);
            when(mapper.entityToDto(user2))
                    .thenReturn(adminUserInfoResponseDto2);

            List<AdminUserInfoResponseDto> actual = adminUserService.getAllUsers();

            assertThat(actual)
                    .hasSize(2)
                    .containsExactlyElementsOf(List.of(adminUserInfoResponseDto, adminUserInfoResponseDto2));
        }
    }

    @Nested
    class CreateUser {
        @Test
        void createUser() {
            when(userRepository.save(any()))
                    .thenReturn(user);
            when(mapper.dtoToEntity(userInfoRequestDto))
                    .thenReturn(user);
            when(mapper.entityToDto(user))
                    .thenReturn(adminUserInfoResponseDto);

            AdminUserInfoResponseDto actual = adminUserService.createUser(userInfoRequestDto);

            assertThat(actual)
                    .isNotNull()
                    .isInstanceOf(AdminUserInfoResponseDto.class)
                    .isEqualTo(adminUserInfoResponseDto);
        }
    }

    @Nested
    class UpdateUser {
        @Test
        void updateUserWhenPresentItDb() {
            UserInfoRequestDto userInfoRequestDto2 = UserInfoRequestDto.builder()
                    .firstAndLastName("Mihail Nekrasov")
                    .role("ROLE_ADMIN")
                    .username("name")
                    .password("pass")
                    .build();
            AdminUserInfoResponseDto expected = adminUserInfoResponseDto2;

            when(userRepository.findById(any()))
                    .thenReturn(Optional.of(user));
            when(userRepository.save(any()))
                    .thenReturn(user2);
            when(mapper.entityToDto(user2))
                    .thenReturn(adminUserInfoResponseDto2);

            AdminUserInfoResponseDto actual = adminUserService.updateUser(userId, userInfoRequestDto2);

            assertThat(actual)
                    .isEqualTo(expected);
            assertThat(actual.getFirstAndLastName())
                    .isEqualTo(expected.getFirstAndLastName());
        }

        @Test
        void updateUserWhenNotPresentItDb() {
            when(userRepository.findById(any()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminUserService.updateUser(userId, userInfoRequestDto))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("Пользователь с таким id не найден в базе данных");
        }
    }

    @Nested
    class DeleteUser {
        @Test
        void deleteUser() {
            String expected = String.format("Пользователь с ID: %s успешно удалён", userId);

            String actual = adminUserService.deleteUser(userId);

            assertThat(actual).isEqualTo(expected);

            verify(userRepository).deleteById(UUID.fromString(userId));
        }
    }
}