package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.UserCardInfo;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.User;
import com.example.bankcards.entity.enums.CardStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CardMapperForUserTest {

    private String userId;
    private String cardId;
    private String cardNumber;
    private String maskedCardNumber;
    private LocalDate expirationDate;
    private Card card;
    private CardStatus cardStatus;
    private UserCardInfo userCardInfo;

    private final CardMapperForUser mapper = new CardMapperForUserImpl();

    @BeforeEach
    void setUp() {
        userId = "54e66dce-e870-4d81-8a1a-fff6d2e07d6b";
        cardId = "bfe0dc37-f16d-4d1f-81c9-ef878a721cab";
        expirationDate = LocalDate.now().plusYears(1);
        cardStatus = CardStatus.ACTIVE;
        cardNumber = "1231 1231 1231 1231";
        maskedCardNumber = "**** **** **** 1231";

        card = Card.builder()
                .cardId(UUID.fromString(cardId))
                .cardNumber(cardNumber)
                .user(User.builder()
                              .userId(UUID.fromString(userId))
                              .build())
                .expirationDate(expirationDate)
                .cardStatus(cardStatus)
                .balance(BigDecimal.valueOf(456.55))
                .build();
        userCardInfo = UserCardInfo.builder()
                .cardNumber(maskedCardNumber)
                .expirationDate(expirationDate)
                .cardStatus(cardStatus.toString())
                .balance(BigDecimal.valueOf(456.55))
                .build();
    }

    @Test
    void fromCardToUserCardInfo() {
        UserCardInfo actual = mapper.fromCardToUserCardInfo(card);
        assertThat(actual).isEqualTo(userCardInfo);
    }

    @Test
    void maskIt() {
        String actual = mapper.maskIt(cardNumber);
        assertThat(actual).isEqualTo(maskedCardNumber);
    }
}