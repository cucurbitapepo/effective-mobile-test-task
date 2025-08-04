package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.UserCardInfo;
import com.example.bankcards.dto.response.UserCardsResponseDto;
import com.example.bankcards.entity.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCardsResponseMapperTest {

    @Mock
    private CardMapperForUserImpl cardMapper;
    @InjectMocks
    private UserCardsResponseMapperImpl mapper;

    private Integer totalPages;
    private List<Card> cards;

    @BeforeEach
    void setUp() {
        totalPages = 1;
        cards = List.of(new Card(), new Card());
    }

    @Test
    void toResponseDto() {
        when(cardMapper.fromCardToUserCardInfo(any(Card.class)))
                .thenReturn(new UserCardInfo());

        UserCardsResponseDto actual = mapper.toResponseDto(totalPages, cards);

        assertThat(actual)
                .isInstanceOf(UserCardsResponseDto.class);
        assertThat(actual.getTotalPages()).isEqualTo(totalPages);
        assertThat(actual.getCards())
                .isNotEmpty()
                .hasSize(2);
    }
}