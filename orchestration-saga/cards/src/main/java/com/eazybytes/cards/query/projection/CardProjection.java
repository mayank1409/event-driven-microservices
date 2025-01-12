package com.eazybytes.cards.query.projection;

import com.eazybytes.cards.command.event.CardCreatedEvent;
import com.eazybytes.cards.command.event.CardDeletedEvent;
import com.eazybytes.cards.command.event.CardUpdatedEvent;
import com.eazybytes.cards.service.ICardsService;
import com.eazybytes.common.event.CardMobileNumberRollbackedEvent;
import com.eazybytes.common.event.CardMobileNumberUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ProcessingGroup("card-group")
public class CardProjection {

    private final ICardsService cardsService;

    @EventHandler
    public void on(CardCreatedEvent cardCreatedEvent) {
        cardsService.createCard(cardCreatedEvent);
    }

    @EventHandler
    public void on(CardUpdatedEvent cardUpdatedEvent) {
        cardsService.updateCard(cardUpdatedEvent);
    }

    @EventHandler
    public void on(CardDeletedEvent cardDeletedEvent) {
        cardsService.deleteCard(cardDeletedEvent);
    }

    @EventHandler
    public void on(CardMobileNumberUpdatedEvent cardMobileNumberUpdatedEvent) {
        cardsService.updateMobileNumber(cardMobileNumberUpdatedEvent.getMobileNumber(), cardMobileNumberUpdatedEvent.getNewMobileNumber());
    }

    @EventHandler
    public void on(CardMobileNumberRollbackedEvent cardMobileNumberRollbackedEvent) {
        cardsService.updateMobileNumber(cardMobileNumberRollbackedEvent.getNewMobileNumber(), cardMobileNumberRollbackedEvent.getMobileNumber());
    }
}
