package com.eazybytes.cards.service;

import com.eazybytes.cards.command.event.CardCreatedEvent;
import com.eazybytes.cards.command.event.CardDeletedEvent;
import com.eazybytes.cards.command.event.CardUpdatedEvent;
import com.eazybytes.cards.dto.CardsDto;

public interface ICardsService {

    /**
     *
     * @param cardCreatedEvent - CardCreatedEvent
     */
    void createCard(CardCreatedEvent cardCreatedEvent);

    /**
     *
     * @param mobileNumber - Input mobile Number
     *  @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);

    /**
     * @param cardUpdatedEvent - CardUpdatedEvent Object
     */
    void updateCard(CardUpdatedEvent cardUpdatedEvent);

    /**
     * @param cardDeletedEvent - CardDeletedEvent Object
     */
    void deleteCard(CardDeletedEvent cardDeletedEvent);
}
