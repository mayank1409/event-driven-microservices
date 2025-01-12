package com.eazybytes.cards.service.impl;

import com.eazybytes.cards.command.event.CardCreatedEvent;
import com.eazybytes.cards.command.event.CardDeletedEvent;
import com.eazybytes.cards.command.event.CardUpdatedEvent;
import com.eazybytes.cards.constants.CardsConstants;
import com.eazybytes.cards.dto.CardsDto;
import com.eazybytes.cards.entity.Cards;
import com.eazybytes.cards.exception.CardAlreadyExistsException;
import com.eazybytes.cards.exception.ResourceNotFoundException;
import com.eazybytes.cards.mapper.CardsMapper;
import com.eazybytes.cards.repository.CardsRepository;
import com.eazybytes.cards.service.ICardsService;
import com.eazybytes.common.event.CardDataChangedEvent;
import lombok.AllArgsConstructor;
import org.axonframework.eventhandling.gateway.EventGateway;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

    private CardsRepository cardsRepository;
    private EventGateway eventGateway;

    /**
     * @param cardCreatedEvent - CardCreatedEvent
     */
    @Override
    public void createCard(CardCreatedEvent cardCreatedEvent) {
        Optional<Cards> optionalCard = cardsRepository.findByMobileNumberAndActiveSw(cardCreatedEvent.getMobileNumber(), CardsConstants.ACTIVE_SW);
        if (optionalCard.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber " + cardCreatedEvent.getMobileNumber());
        }
        Cards card = createNewCard(cardCreatedEvent.getCardNumber(), cardCreatedEvent.getMobileNumber());
        cardsRepository.save(card);
    }

    /**
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createNewCard(long cardNumber, String mobileNumber) {
        Cards newCard = new Cards();
        newCard.setCardNumber(cardNumber);
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
        newCard.setActiveSw(CardsConstants.ACTIVE_SW);
        return newCard;
    }

    /**
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards card = cardsRepository.findByMobileNumberAndActiveSw(mobileNumber, CardsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
        return CardsMapper.mapToCardsDto(card, new CardsDto());
    }

    /**
     * @param cardUpdatedEvent - CardUpdatedEvent Object
     */
    @Override
    public void updateCard(CardUpdatedEvent cardUpdatedEvent) {
        Cards card = cardsRepository.findByMobileNumberAndActiveSw(cardUpdatedEvent.getMobileNumber(), CardsConstants.ACTIVE_SW)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "CardNumber", cardUpdatedEvent.getCardNumber().toString()));
        CardsMapper.mapEventToCard(cardUpdatedEvent, card);
        cardsRepository.save(card);
    }

    /**
     * @param cardDeletedEvent - CardDeletedEvent Object
     */
    @Override
    public void deleteCard(CardDeletedEvent cardDeletedEvent) {
        Cards card = cardsRepository.findById(cardDeletedEvent.getCardNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardDeletedEvent.getCardNumber().toString()));
        card.setActiveSw(CardsConstants.IN_ACTIVE_SW);
        cardsRepository.save(card);
        CardDataChangedEvent cardDataChangedEvent = new CardDataChangedEvent();
        cardDataChangedEvent.setMobileNumber(card.getMobileNumber());
        cardDataChangedEvent.setCardNumber(0L);
        eventGateway.publish(cardDataChangedEvent);
    }
}
