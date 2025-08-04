package com.example.bankcards.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.bankcards.dto.request.CardInfoRequestDto;
import com.example.bankcards.dto.response.AdminCardInfoResponseDto;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.handler.ControllersExceptionHandler;
import com.example.bankcards.service.AdminCardService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
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
class AdminCardControllerTest {
    @Mock
    private AdminCardService adminCardService;

    @InjectMocks
    private AdminCardController adminCardController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AdminCardInfoResponseDto responseDto;
    private CardInfoRequestDto requestDto;
    private String userId;
    private String cardId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(adminCardController)
                .setControllerAdvice(new ControllersExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

        userId = "48fbd421-27d7-487e-b1bb-4b38fe0aba58";
        cardId = "8b52c2e1-8641-4fa9-b983-56042d6ed600";
        responseDto = AdminCardInfoResponseDto.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber("1234 5678 9101 1213")
                .firstAndLastName("John Doe")
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus("ACTIVE")
                .balance(BigDecimal.valueOf(22.2))
                .build();
        requestDto = CardInfoRequestDto.builder()
                .cardNumber("1234 5678 9101 1213")
                .userId(userId)
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus("ACTIVE")
                .balance(BigDecimal.valueOf(22.2))
                .build();
    }

    @Nested
    class GetCardInformationByCardId {
        @Test
        @SneakyThrows
        void getCardInformationByCardId_whenAllDataCorrect() {
            when(adminCardService.getCardInformationById(cardId))
                    .thenReturn(responseDto);

            mockMvc.perform(get("/admin/cards/{cardId}", cardId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.cardNumber").value("1234 5678 9101 1213"))
                    .andExpect(jsonPath("$.firstAndLastName").value("John Doe"))
                    .andExpect(jsonPath("$.cardId").value(cardId))
                    .andDo(print());

            verify(adminCardService).getCardInformationById(cardId);
        }

        @Test
        @SneakyThrows
        void getCardInformationByCardId_whenUserNotFound() {
            when(adminCardService.getCardInformationById(cardId))
                    .thenThrow(new CardNotFoundException());

            mockMvc.perform(get("/admin/cards/{cardId}", cardId))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Карта с таким id не найдена в базе данных"))
                    .andDo(print());

            verify(adminCardService).getCardInformationById(cardId);
        }
    }

    @Nested
    class GetAllCardsInformation {
        @Test
        @SneakyThrows
        void getAllCardsInformation() {
            when(adminCardService.getAllCards())
                    .thenReturn(List.of(responseDto, responseDto));
            mockMvc.perform(get("/admin/cards/all"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.length()").value(2))
                    .andDo(print());

            verify(adminCardService).getAllCards();
        }
    }

    @Nested
    class CreateCard {
        @Test
        @SneakyThrows
        void createCard_whenValidInput_returnsCreatedCard() {
            when(adminCardService.createCard(any(CardInfoRequestDto.class)))
                    .thenReturn(responseDto);

            mockMvc.perform(post("/admin/cards")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.cardNumber").value("1234 5678 9101 1213"))
                    .andExpect(jsonPath("$.firstAndLastName").value("John Doe"))
                    .andExpect(jsonPath("$.cardId").value(cardId))
                    .andDo(print());

            verify(adminCardService).createCard(requestDto);
        }

        @Test
        @SneakyThrows
        void createCard_whenInvalidInput_returnsBadRequest() {
            requestDto.setCardNumber("bla bla bla");

            mockMvc.perform(post("/admin/cards")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(requestDto)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Номер карты должен выглядеть как 16 цифр в блоках по 4 разделенных пробелом."))
                    .andDo(print());

            verifyNoInteractions(adminCardService);
        }
    }

    @Nested
    class ChangeStatus {
        @Test
        @SneakyThrows
        void changeStatus_whenReturnsUpdatedCard() {
            String status = "ACTIVE";
            when(adminCardService.changeStatus(cardId, status))
                    .thenReturn(String.format("Статус карты с id %s успешно изменен", cardId));
            mockMvc.perform(put("/admin/cards/")
                                    .param("cardId", cardId)
                                    .param("status", status))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.format("Статус карты с id %s успешно изменен", cardId)))
                    .andDo(print());

            verify(adminCardService).changeStatus(cardId, status);
        }

        @Test
        @SneakyThrows
        void updateUser_whenCardNotFound_returnsNotFound() {
            String status = "ACTIVE";
            when(adminCardService.changeStatus(cardId, status))
                    .thenThrow(new CardNotFoundException());

            mockMvc.perform(put("/admin/cards/")
                                    .param("cardId", cardId)
                                    .param("status", status))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.error").value("Карта с таким id не найдена в базе данных"))
                    .andDo(print());

            verify(adminCardService).changeStatus(cardId, status);
        }
    }

    @Nested
    class DeleteCard {
        @Test
        @SneakyThrows
        void deleteCard() {
            when(adminCardService.delete(cardId))
                    .thenReturn(String.format("Карта с ID: %s успешно удалёна", cardId));

            mockMvc.perform(delete("/admin/cards/{cardId}", cardId))
                    .andExpect(status().isOk())
                    .andExpect(content().string(String.format("Карта с ID: %s успешно удалёна", cardId)))
                    .andDo(print());

            verify(adminCardService).delete(cardId);
        }
    }
}