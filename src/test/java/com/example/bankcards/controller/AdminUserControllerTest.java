package com.example.bankcards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bankcards.dto.request.UserInfoRequestDto;
import com.example.bankcards.dto.response.AdminUserInfoResponseDto;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.exception.handler.ControllersExceptionHandler;
import com.example.bankcards.service.AdminUserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AdminUserControllerTest {

    @Mock
    private AdminUserService adminUserService;

    @InjectMocks
    private AdminUserController adminUserController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AdminUserInfoResponseDto responseDto;
    private String userId;
    private UserInfoRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminUserController)
                .setControllerAdvice(new ControllersExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        userId = "48fbd421-27d7-487e-b1bb-4b38fe0aba58";
        responseDto = AdminUserInfoResponseDto.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("John Doe")
                .role("ROLE_USER")
                .build();
        requestDto = UserInfoRequestDto.builder()
                .firstAndLastName("John Doe")
                .username("Misha")
                .password("pass")
                .role("ROLE_USER")
                .build();
    }

    @Nested
    class GetUserInformationByUserId {
        @Test
        @SneakyThrows
        void getUserInformationByUserId_whenAllDataCorrect() {
            when(adminUserService.getUserInformationById(userId))
                    .thenReturn(responseDto);

            mockMvc.perform(get("/admin/users/{userId}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstAndLastName").value("John Doe"))
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andDo(print());

            verify(adminUserService).getUserInformationById(userId);
        }

        @Test
        @SneakyThrows
        void getUserInformationByUserId_whenUserNotFound() {
            when(adminUserService.getUserInformationById(userId))
                    .thenThrow(new UserNotFoundException());

            mockMvc.perform(get("/admin/users/{userId}", userId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Пользователь с таким id не найден в базе данных"))
                    .andDo(print());

            verify(adminUserService).getUserInformationById(userId);
        }
    }

    @Nested
    class GetAllUserInformation {
        @Test
        @SneakyThrows
        void getAllUserInformation() {
            when(adminUserService.getAllUsers())
                    .thenReturn(List.of(responseDto, responseDto));
            mockMvc.perform(get("/admin/users/all"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2))
                    .andDo(print());

            verify(adminUserService).getAllUsers();
        }
    }

    @Nested
    class CreateUser {
        @Test
        @SneakyThrows
        void createUser_whenValidInput_returnsCreatedUser() {
            when(adminUserService.createUser(any(UserInfoRequestDto.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(post("/admin/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstAndLastName").value("John Doe"))
                    .andExpect(jsonPath("$.role").value("ROLE_USER"))
                    .andExpect(jsonPath("$.userId").value(userId))
                    .andDo(print());

            verify(adminUserService).createUser(any(UserInfoRequestDto.class));
        }

        @Test
        @SneakyThrows
        void createUser_whenInvalidFirstAndLastName_returnsBadRequest() {
            UserInfoRequestDto invalidRequestDto = requestDto;
            requestDto.setFirstAndLastName("");

            mockMvc.perform(post("/admin/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(invalidRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Фамилия и имя пользователя не могут быть пустыми."))
                    .andDo(print());

            verifyNoInteractions(adminUserService);
        }

        @Test
        @SneakyThrows
        void createUser_whenInvalidRole_returnsBadRequest() {
            UserInfoRequestDto invalidRequestDto = requestDto;
            requestDto.setRole("bla bla bla");

            mockMvc.perform(post("/admin/users")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(invalidRequestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Роль пользователя может быть только ROLE_USER или ROLE_ADMIN."))
                    .andDo(print());

            verifyNoInteractions(adminUserService);
        }
    }

    @Nested
    class UpdateUser {
        @Test
        @SneakyThrows
        void updateUser_whenReturnsUpdatedUser() {
            responseDto.setFirstAndLastName("Mikhail Naumov");
            when(adminUserService.updateUser(userId, requestDto))
                    .thenReturn(responseDto);

            mockMvc.perform(put("/admin/users/{userId}", userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.firstAndLastName").value("Mikhail Naumov"))
                    .andDo(print());

            verify(adminUserService).updateUser(userId, requestDto);
        }

        @Test
        @SneakyThrows
        void updateUser_whenUserNotFound_returnsNotFound() {
            when(adminUserService.updateUser(eq(userId), any(UserInfoRequestDto.class)))
                    .thenThrow(new UserNotFoundException());

            mockMvc.perform(put("/admin/users/{userId}", userId)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Пользователь с таким id не найден в базе данных"))
                    .andDo(print());

            verify(adminUserService).updateUser(userId, requestDto);
        }
    }

    @Nested
    class DeleteUser {
        @Test
        @SneakyThrows
        void deleteUser() {
            when(adminUserService.deleteUser(userId))
                    .thenReturn(String.format("Пользователь с ID: %s успешно удалён", userId));

            mockMvc.perform(delete("/admin/users/{userId}", userId))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.format("Пользователь с ID: %s успешно удалён", userId)))
                    .andDo(print());

            verify(adminUserService).deleteUser(userId);
        }
    }
}