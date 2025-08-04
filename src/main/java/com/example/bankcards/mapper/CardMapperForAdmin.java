package com.example.bankcards.mapper;

import com.example.bankcards.dto.request.CardInfoRequestDto;
import com.example.bankcards.dto.response.AdminCardInfoResponseDto;
import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {CardStatus.class})
public interface CardMapperForAdmin {
    @Mapping(target = "firstAndLastName", expression = "java(card.getUser().getFirstAndLastName())")
    AdminCardInfoResponseDto toFullCardInfoResponseDto(Card card);

    @Mapping(target = "cardId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "cardStatus", expression = "java(CardStatus.valueOf(cardInfoRequestDto.getCardStatus()))")
    Card toCard(CardInfoRequestDto cardInfoRequestDto);
}
