package com.example.bankcards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bankcards.dto.response.UserCardInfo;
import com.example.bankcards.dto.response.UserCardsResponseDto;
import com.example.bankcards.exception.handler.ControllersExceptionHandler;
import com.example.bankcards.service.UserService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserControllerTest {

    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UserCardsResponseDto responseDto;
    private LocalDate expirationDate;
    private String userId;
    private String cardId;
    private String cardId2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setControllerAdvice(new ControllersExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();

        userId = "48fbd421-27d7-487e-b1bb-4b38fe0aba58";
        cardId = "c9a692ba-7e4e-45ed-955b-91527b71e3d4";
        cardId2 = "55a692ba-7e4e-45ed-955b-91527b71e3d4";
        expirationDate = LocalDate.now().plusYears(1);

        responseDto = UserCardsResponseDto.builder()
                .totalPages(1)
                .cards(List.of(UserCardInfo.builder().build(), UserCardInfo.builder().build()))
                .build();
    }

    @Nested
    class GetAllCardsInformation {
        @Test
        @SneakyThrows
        void getAllCardsInformation_withParameters() {
            String cardStatus = "ACTIVE";
            int page = 2;
            int pageSize = 10;

            when(userService.getAllCards(userId, expirationDate.minusYears(1), expirationDate, cardStatus, page, pageSize))
                    .thenReturn(responseDto);

            mockMvc.perform(get("/user/all")
                                    .header("userId", userId)
                                    .param("expireFrom", expirationDate.minusYears(1).toString())
                                    .param("expireTo", expirationDate.toString())
                                    .param("cardStatus", cardStatus)
                                    .param("page", String.valueOf(page))
                                    .param("pageSize", String.valueOf(pageSize))
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(responseDto)))
                    .andDo(print());
        }

        @Test
        @SneakyThrows
        void getAllCardsInformation_withoutParameters() {
            when(userService.getAllCards(userId, null, null, null, 1, 5))
                    .thenReturn(responseDto);

            mockMvc.perform(get("/user/all")
                                    .header("userId", userId)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
        }

        @Test
        @SneakyThrows
        void getAllCardsInformation_InvalidUUID_ReturnsBadRequest() {
            String invalidUserId = "invalid-uuid";

            mockMvc.perform(get("/user/all")
                                    .header("userId", invalidUserId)
                                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @SneakyThrows
    void blockCard_whenOk() {
        String successMessage = "Карта успешно заблокирована";

        when(userService.block(userId, cardId)).thenReturn(successMessage);

        mockMvc.perform(put("/user/block/{cardId}", cardId)
                                .header("userId", userId)
                                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(successMessage));
    }

    @Nested
    class TransferBetweenCards {
        @Test
        @SneakyThrows
        void transferBetweenCards_whenOk() {
            String successMessage = "Сумма успешно переведена между картами";
            double amount = 55.5;

            when(userService.transfer(userId, cardId, cardId2, BigDecimal.valueOf(amount)))
                    .thenReturn(successMessage);

            mockMvc.perform(put("/user/transfer")
                                    .header("userId", userId)
                                    .param("idCardFrom", cardId)
                                    .param("idCardTo", cardId2)
                                    .param("amount", String.valueOf(amount))
                                    .accept(MediaType.TEXT_PLAIN))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string(successMessage));
        }

        @Test
        @SneakyThrows
        void transferBetweenCards_whenForgetAmount() {
            mockMvc.perform(put("/user/transfer")
                                    .header("userId", userId)
                                    .param("idCardFrom", cardId)
                                    .param("idCardTo", cardId2)
                                    .accept(MediaType.TEXT_PLAIN))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void getBalance_ReturnsOkAndBalanceString() throws Exception {
        String balance = "1500.75";

        when(userService.getBalance(userId, cardId)).thenReturn(balance);

        mockMvc.perform(get("/user/balance/{cardId}", cardId)
                                .header("userId", userId)
                                .accept(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(balance));
    }
}