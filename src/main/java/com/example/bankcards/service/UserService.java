package com.example.bankcards.service;

import com.example.bankcards.dto.response.UserCardsResponseDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserService {

    UserCardsResponseDto getAllCards(String userId, LocalDate expireFrom, LocalDate expireTo,
                                     String cardStatus, int page, int pageSize);

    String block(String userId, String cardId);

    String transfer(String userId, String idCardFrom, String idCardTo, BigDecimal amount);

    String getBalance(String userId, String cardId);
}
