package com.example.bankcards.service;

import com.example.bankcards.dto.request.CardInfoRequestDto;
import com.example.bankcards.dto.response.AdminCardInfoResponseDto;

import java.util.List;

public interface AdminCardService {
    AdminCardInfoResponseDto getCardInformationById(String cardId);

    AdminCardInfoResponseDto createCard(CardInfoRequestDto cardInfoRequestDto);

    String changeStatus(String cardId, String status);

    String delete(String cardId);

    List<AdminCardInfoResponseDto> getAllCards();
}
