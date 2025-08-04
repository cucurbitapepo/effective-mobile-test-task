package com.example.bankcards.mapper;

import com.example.bankcards.dto.response.UserCardInfo;
import com.example.bankcards.dto.response.UserCardsResponseDto;
import com.example.bankcards.entity.Card;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CardMapperForUser.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserCardsResponseMapper {
    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "cards", source = "cards")
    UserCardsResponseDto toResponseDto(Integer totalPages, List<Card> cards);

    List<UserCardInfo> fromCardsToUserCardInfos(List<Card> cards);
}
