package com.example.bankcards.service.impl;

import com.example.bankcards.dto.response.UserCardsResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import com.example.bankcards.exception.CardNotBelongsToUserException;
import com.example.bankcards.exception.CardNotFoundException;
import com.example.bankcards.exception.NotEnoughMoneyException;
import com.example.bankcards.mapper.UserCardsResponseMapper;
import com.example.bankcards.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private UserCardsResponseMapper userCardsResponseMapper;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private String userId;
    private String cardId;
    private String cardId2;
    private LocalDate expirationDate;
    private String cardStatus;
    private int page;
    private int pageSize;
    private Card card;
    private List<Card> cards;
    private Page<Card> cardsPage;

    @BeforeEach
    void setUp() {
        userId = "54e66dce-e870-4d81-8a1a-fff6d2e07d6b";
        cardId = "bfe0dc37-f16d-4d1f-81c9-ef878a721cab";
        cardId2 = "3c24223c-ac3d-4c91-bf7b-dbe625580261";
        expirationDate = LocalDate.now().plusYears(1);
        cardStatus = "ACTIVE";
        page = 1;
        pageSize = 10;
        card = Card.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber("1231 1231 1231 1231")
                .user(User.builder()
                              .userId(UUID.fromString(userId))
                              .build())
                .expirationDate(expirationDate)
                .cardStatus(CardStatus.valueOf(cardStatus))
                .balance(BigDecimal.valueOf(456.55))
                .build();
        cards = new ArrayList<>();
        cards.add(card);
        cards.add(Card.builder()
                          .cardId(UUID.fromString(cardId2))
                          .cardNumber("3211 1231 1231 1231")
                          .user(User.builder()
                                        .userId(UUID.fromString(userId))
                                        .build())
                          .expirationDate(expirationDate)
                          .cardStatus(CardStatus.valueOf(cardStatus))
                          .balance(BigDecimal.valueOf(500.0))
                          .build());
        cardsPage = new PageImpl<>(cards, PageRequest.of(page - 1, pageSize), 2);
    }

    @Nested
    class GetAllCards {
        @Test
        @SuppressWarnings(value = "unchecked")
        void getAllCards_returnsExpectedDto() {
            UserCardsResponseDto expected = UserCardsResponseDto.builder().build();

            when(cardRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(cardsPage);
            when(userCardsResponseMapper.toResponseDto(cardsPage.getTotalPages(), cardsPage.getContent()))
                    .thenReturn(expected);

            UserCardsResponseDto actual = userServiceImpl.getAllCards(userId, expirationDate,
                                                                      expirationDate.plusYears(2), cardStatus,
                                                                      page, pageSize);

            assertThat(actual)
                    .isEqualTo(expected);

            ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

            verify(cardRepository).findAll(any(Specification.class), pageableCaptor.capture());

            Pageable pageable = pageableCaptor.getValue();
            assertEquals(page - 1, pageable.getPageNumber());
            assertEquals(pageSize, pageable.getPageSize());
        }

        @Test
        void getAllCards_withoutFilters() {
            UserCardsResponseDto expected = UserCardsResponseDto.builder().build();

            when(cardRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(cardsPage);
            when(userCardsResponseMapper.toResponseDto(cardsPage.getTotalPages(), cardsPage.getContent()))
                    .thenReturn(expected);

            UserCardsResponseDto actual = userServiceImpl.getAllCards(userId, null, null, null,
                                                                      page, pageSize);

            assertThat(actual)
                    .isEqualTo(expected);
        }
    }

    @Nested
    class Block {
        @Test
        void block_whenCardBelongsToUser() {
            String expected = "Карта успешно заблокирована";

            when(cardRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(card));

            String actual = userServiceImpl.block(userId, cardId);

            assertThat(actual)
                    .isEqualTo(expected);
        }

        @Test
        void block_whenCardNotBelongsToUser() {
            when(cardRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(card));

            assertThatThrownBy(() -> userServiceImpl.block(UUID.randomUUID().toString(), cardId))
                    .isInstanceOf(CardNotBelongsToUserException.class)
                    .hasMessageContaining("Операция невозможна. Карта не принадлежит этому пользователю");
        }

        @Test
        void block_whenCardNotFound() {
            when(cardRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> userServiceImpl.block(UUID.randomUUID().toString(), cardId))
                    .isInstanceOf(CardNotFoundException.class)
                    .hasMessageContaining("Карта с таким id не найдена в базе данных");
        }
    }

    @Nested
    class Transfer {
        @Test
        void transfer_whenEnoughMoney() {
            String expected = "Сумма успешно переведена между картами";

            when(cardRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(card));

            String actual = userServiceImpl.transfer(userId, cardId, cardId2, BigDecimal.valueOf(5));

            assertThat(actual)
                    .isEqualTo(expected);
        }

        @Test
        void transfer_whenNotEnoughMoney() {
            when(cardRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(card));

            assertThatThrownBy(() -> userServiceImpl.transfer(userId, cardId, cardId2, BigDecimal.valueOf(500000)))
                    .isInstanceOf(NotEnoughMoneyException.class)
                    .hasMessageContaining("На карте недостаточно средств для перевода");
        }
    }

    @Nested
    class GetBalance {
        @Test
        void getBalance() {
            String expected = card.getBalance().toString();

            when(cardRepository.findById(any(UUID.class)))
                    .thenReturn(Optional.of(card));

            String actual = userServiceImpl.getBalance(userId, cardId);

            assertThat(actual).isEqualTo(expected);
        }
    }
}