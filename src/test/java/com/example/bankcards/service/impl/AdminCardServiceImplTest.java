package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.CardInfoRequestDto;
import com.example.bankcards.dto.response.AdminCardInfoResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.UserNotFoundException;
import com.example.bankcards.mapper.CardMapperForAdmin;
import com.example.bankcards.repository.CardRepository;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCardServiceImplTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardMapperForAdmin mapper;
    @InjectMocks
    private AdminCardServiceImpl adminCardService;

    private String cardId;
    private String cardId2;
    private String userId;
    private User user;
    private Card card;
    private Card card2;
    private AdminCardInfoResponseDto adminCardInfoResponseDto;
    private AdminCardInfoResponseDto adminCardInfoResponseDto2;
    private CardInfoRequestDto cardInfoRequestDto;

    @BeforeEach
    void setUp() {
        cardId = "e619c532-218e-4f2c-8d30-c75cf29e0276";
        cardId2 = "af5f2191-4c4c-4bf3-983d-f02caf7b4c38";
        userId = "e487f331-5b7d-40b9-b49d-b41f94b2c960";
        user = User.builder()
                .userId(UUID.fromString(userId))
                .firstAndLastName("John Doe")
                .build();
        card = Card.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber("1230 4560 7890 0120")
                .user(user)
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(22.2))
                .build();
        card2 = Card.builder()
                .cardId(UUID.fromString(cardId2))
                .cardNumber("9999 4560 7890 9999")
                .user(user)
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus(CardStatus.ACTIVE)
                .balance(BigDecimal.valueOf(33.3))
                .build();
        adminCardInfoResponseDto = AdminCardInfoResponseDto.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber("1230 4560 7890 0120")
                .firstAndLastName("John Doe")
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus(CardStatus.ACTIVE.toString())
                .balance(BigDecimal.valueOf(22.2))
                .build();
        adminCardInfoResponseDto2 = AdminCardInfoResponseDto.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber("9999 4560 7890 9999")
                .firstAndLastName("John Doe")
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus(CardStatus.ACTIVE.toString())
                .balance(BigDecimal.valueOf(33.3))
                .build();
        cardInfoRequestDto = CardInfoRequestDto.builder()
                .cardNumber("1230 4560 7890 0120")
                .userId(userId)
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus("ACTIVE")
                .balance(BigDecimal.valueOf(22.2))
                .build();
    }

    @Nested
    class GetCardInformationById {
        @Test
        void getCardInformationByIdWhenPresent() {
            when(cardRepository.findById(any()))
                    .thenReturn(Optional.of(card));
            when(mapper.toFullCardInfoResponseDto(card))
                    .thenReturn(adminCardInfoResponseDto);

            AdminCardInfoResponseDto actual = adminCardService.getCardInformationById(cardId);

            assertThat(actual)
                    .isEqualTo(adminCardInfoResponseDto);
        }

        @Test
        void getCardInformationByIdWhenNotPresent() {
            when(cardRepository.findById(any()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminCardService.getCardInformationById(cardId))
                    .isInstanceOf(CardNotFoundException.class)
                    .hasMessageContaining("Карта с таким id не найдена в базе данных");
        }
    }

    @Nested
    class GetAllCards {
        @Test
        void getAllCards() {
            when(cardRepository.findAll())
                    .thenReturn(List.of(card, card2));
            when(mapper.toFullCardInfoResponseDto(card))
                    .thenReturn(adminCardInfoResponseDto);
            when(mapper.toFullCardInfoResponseDto(card2))
                    .thenReturn(adminCardInfoResponseDto2);

            List<AdminCardInfoResponseDto> actual = adminCardService.getAllCards();

            assertThat(actual)
                    .hasSize(2)
                    .containsExactlyElementsOf(List.of(adminCardInfoResponseDto, adminCardInfoResponseDto2));
        }
    }

    @Nested
    class CreateCard {
        @Test
        void createCardWhenUserPresent() {
            when(userRepository.findById(any()))
                    .thenReturn(Optional.of(user));
            when(mapper.toCard(cardInfoRequestDto))
                    .thenReturn(card);
            when(cardRepository.save(card))
                    .thenReturn(card);
            when(mapper.toFullCardInfoResponseDto(card))
                    .thenReturn(adminCardInfoResponseDto);

            AdminCardInfoResponseDto actual = adminCardService.createCard(cardInfoRequestDto);

            assertThat(actual)
                    .isEqualTo(adminCardInfoResponseDto);
        }

        @Test
        void createCardWhenUserNotFound() {
            when(userRepository.findById(any()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminCardService.createCard(cardInfoRequestDto))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessageContaining("Пользователь с таким id не найден в базе данных");
        }
    }

    @Nested
    class ChangeStatus {
        @Test
        void changeStatusWhenCardPresent() {
            String expected = String.format("Статус карты с id %s успешно изменен", cardId);

            when(cardRepository.findById(any()))
                    .thenReturn(Optional.of(card));

            String actual = adminCardService.changeStatus(cardId, "ACTIVE");

            assertThat(actual)
                    .isEqualTo(expected);
        }

        @Test
        void changeStatusWhenCardNotPresent() {
            when(cardRepository.findById(any()))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> adminCardService.changeStatus(cardId, "ACTIVE"))
                    .isInstanceOf(CardNotFoundException.class)
                    .hasMessageContaining("Карта с таким id не найдена в базе данных");
        }
    }

    @Nested
    class Delete {
        @Test
        void delete() {
            String expected = String.format("Карта с ID: %s успешно удалёна", cardId);

            String actual = adminCardService.delete(cardId);

            assertThat(actual).isEqualTo(expected);
        }
    }
}