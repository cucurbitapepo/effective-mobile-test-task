package com.example.bankcards.service.specification;

import com.example.bankcards.entity.Card;
import com.example.bankcards.entity.enums.CardStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
public class CardFilterSpecification {

    public static Specification<Card> hasUserId(UUID userId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("userId"), userId);
    }

    public static Specification<Card> hasExpirationDateAfterOrEqual(LocalDate expireFrom) {
        return (root, query, criteriaBuilder) ->
                expireFrom == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("expirationDate"), expireFrom);
    }

    public static Specification<Card> hasExpirationDateBeforeOrEqual(LocalDate endDate) {
        return (root, query, criteriaBuilder) ->
                endDate == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.lessThanOrEqualTo(root.get("expirationDate"), endDate);
    }

    public static Specification<Card> hasCardStatus(CardStatus cardStatus) {
        return (root, query, criteriaBuilder) ->
                cardStatus == null ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("cardStatus"), cardStatus.toString());
    }
}
