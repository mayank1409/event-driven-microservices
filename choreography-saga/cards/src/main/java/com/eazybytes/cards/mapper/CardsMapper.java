package com.eazybytes.cards.mapper;

import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entity.Cards;

public class CardsMapper {

    public static CardsDto mapToCardsDto(Cards card, CardsDto cardsDto) {
        cardsDto.setCardNumber(card.getCardNumber());
        cardsDto.setCardType(card.getCardType());
        cardsDto.setMobileNumber(card.getMobileNumber());
        cardsDto.setTotalLimit(card.getTotalLimit());
        cardsDto.setAvailableAmount(card.getAvailableAmount());
        cardsDto.setAmountUsed(card.getAmountUsed());
        cardsDto.setActiveSw(card.isActiveSw());
        return cardsDto;
    }

    public static void mapToCards(CardsDto cardsDto, Cards card) {
        card.setCardType(cardsDto.getCardType());
        card.setTotalLimit(cardsDto.getTotalLimit());
        card.setAvailableAmount(cardsDto.getAvailableAmount());
        card.setAmountUsed(cardsDto.getAmountUsed());
    }
}
