package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardInfoRequestDto;
import com.example.bankcards.dto.response.AdminCardInfoResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CardMapperForAdminTest {
    private final CardMapperForAdmin mapper = new CardMapperForAdminImpl();

    private String cardId;
    private String userId;
    private User user;
    private Card card;
    private AdminCardInfoResponseDto adminCardInfoResponseDto;
    private CardInfoRequestDto cardInfoRequestDto;

    @BeforeEach
    void setUp() {
        cardId = "e619c532-218e-4f2c-8d30-c75cf29e0276";
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
        adminCardInfoResponseDto = AdminCardInfoResponseDto.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber("1230 4560 7890 0120")
                .firstAndLastName("John Doe")
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus(CardStatus.ACTIVE.toString())
                .balance(BigDecimal.valueOf(22.2))
                .build();
        cardInfoRequestDto = CardInfoRequestDto.builder()
                .cardNumber("1230 4560 7890 0120")
                .userId(userId)
                .expirationDate(LocalDate.now().plusYears(1))
                .cardStatus("ACTIVE")
                .balance(BigDecimal.valueOf(22.2))
                .build();
    }

    @Test
    void toFullCardInfoResponseDto() {
        AdminCardInfoResponseDto actual = mapper.toFullCardInfoResponseDto(card);

        assertThat(actual).isEqualTo(adminCardInfoResponseDto);
    }

    @Test
    void toCard() {
        card.setCardId(null);
        card.setUser(null);

        Card actual = mapper.toCard(cardInfoRequestDto);

        assertThat(actual).isEqualTo(card);
    }

}