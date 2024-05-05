package com.whiskeysierra.red7;

import java.util.ArrayList;

public class Player {
    protected Game game;

    protected int id;
    protected boolean isInGame;
    protected Palette palette;
    protected Hand hand;
    protected Card intentPlayCard;
    protected Card intentDiscardCard;
    protected boolean isWinIntentPlayCard;      // Нужно их сбрасывать после каждого нового вызова или в начале хода
    protected boolean isWinIntentDiscardCard;

    public Player(int id, Game game) {
        this.id = id;
        this.game = game;
        isInGame = true;
        palette = new Palette();
        hand    = new Hand();

        palette.addCard(game.deck.returnCard());
        for (int i = 0; i < 7; ++i) hand.addCard(game.deck.returnCard());
    }

    public void playCardFromHandToPalette(int indexOfCard) {
        if (0 <= indexOfCard && indexOfCard < hand.cards.size()) {
            palette.addCard(hand.cards.get(indexOfCard));
            hand.removeCard(indexOfCard);
        }
        //else
            //System.out.println("There isn't card in hand with index : " + indexOfCard);
    }

    public void playCardFromHandToPalette(Card card) {
        if (hand.cards.contains(card)) {
            palette.addCard(card);
            hand.removeCard(card);
        }
        //else
            //System.out.println("There isn't such card in hand : " + card);
    }

    public void discardCardFromHandToRulesPile(int indexOfCard) {
        if (0 <= indexOfCard && indexOfCard < hand.cards.size()) {
            game.rulesPile = hand.cards.get(indexOfCard);
            hand.removeCard(indexOfCard);
        }
        //else
            //System.out.println("There isn't card in hand with index : " + indexOfCard);
    }

    public void discardCardFromHandToRulesPile(Card card) {
        if (hand.cards.contains(card)) {
            game.rulesPile = card;
            hand.removeCard(card);
        }
        //else
            //System.out.println("There isn't such card in hand : " + card);
    }

    public void tryToPlayCard(int indexOfCard) {
        if (0 <= indexOfCard && indexOfCard < hand.cards.size()) {
            this.intentPlayCard = hand.cards.get(indexOfCard);

            palette.addCard(this.intentPlayCard);
            isWinIntentPlayCard = game.getIdWinner(game.rulesPile) == id;
            palette.removeCard(intentPlayCard);
        }
        else {
            //System.out.println("There isn't card in hand with index : " + indexOfCard);
        }
    }

    public void tryToPlayCard(Card intentCard) {
        if (hand.cards.contains(intentCard)) {
            this.intentPlayCard = intentCard;

            palette.addCard(this.intentPlayCard);
            isWinIntentPlayCard = game.getIdWinner(game.rulesPile) == id;
            palette.removeCard(intentPlayCard);
        }
        else {
            //System.out.println("There isn't such card in hand : " + intentCard);
        }
    }

    public void tryToDiscardCard(int indexOfCard) {
        if (0 <= indexOfCard && indexOfCard < hand.cards.size()) {
            intentDiscardCard = hand.cards.get(indexOfCard);
            isWinIntentDiscardCard = game.getIdWinner(intentDiscardCard) == id;
        }
        else {
            //System.out.println("There isn't card in hand with index : " + indexOfCard);
        }
    }

    public void tryToDiscardCard(Card intentCard) {
        if (hand.cards.contains(intentCard)) {
            intentDiscardCard = intentCard;
            isWinIntentDiscardCard = game.getIdWinner(intentCard) == id;
        }
        else {
            //System.out.println("There isn't such card in hand");
        }
    }

    /**
     id карт нельзя менять до конца хода (в текущем ходу)
     */
    public void tryToPlayThenDiscardCard(int indexOfIntentPlayCard, int indexOfIntentDiscardCard) {
        if      (0 <= indexOfIntentPlayCard && 0 <= indexOfIntentDiscardCard &&
                indexOfIntentPlayCard < hand.cards.size() && indexOfIntentDiscardCard < hand.cards.size() &&
                indexOfIntentPlayCard != indexOfIntentDiscardCard) {

            intentPlayCard = hand.cards.get(indexOfIntentPlayCard);
            intentDiscardCard = hand.cards.get(indexOfIntentDiscardCard);

            palette.addCard(intentPlayCard);

            if (game.getIdWinner(intentDiscardCard) == id) {
                isWinIntentPlayCard = true;
                isWinIntentDiscardCard = true;
            }
            else {
                isWinIntentPlayCard = false;
                isWinIntentDiscardCard = false;
            }

            palette.removeCard(this.intentPlayCard);
        }
        else {
            //System.out.println("Wrong index(es) : " + indexOfIntentPlayCard + " or " + indexOfIntentDiscardCard);
        }
    }

    public void tryToPlayThenDiscardCard(Card intentPlayCard, Card intentDiscardCard) {
        if (hand.cards.contains(intentPlayCard) && hand.cards.contains(intentDiscardCard)) {
            this.intentPlayCard = intentPlayCard;
            this.intentDiscardCard = intentDiscardCard;

            palette.addCard(this.intentPlayCard);

            if (game.getIdWinner(this.intentDiscardCard) == id) {
                isWinIntentPlayCard = true;
                isWinIntentDiscardCard = true;
            }
            else {
                isWinIntentPlayCard = false;
                isWinIntentDiscardCard = false;
            }

            palette.removeCard(intentPlayCard);
        }
        else {
            //System.out.println("There isn't such card in hand : " + intentPlayCard + " or " + intentDiscardCard);
        }
    }

    public ArrayList<Card> getRuledCards(Card cardFromRulesPile) {
        return palette.getRuledCards(cardFromRulesPile);
    }

    public int doTurn() {
        if (hand.cards.isEmpty()) {
            isInGame = false;
            return 0;
        }
        for (int i = 0; i < hand.cards.size(); i++) {
            tryToPlayCard(i);
            if (isWinIntentPlayCard) {
                playCardFromHandToPalette(i);
                isWinIntentPlayCard = false;
                return 1;
            }
        }
        for (int i = 0; i < hand.cards.size(); i++) {
            tryToDiscardCard(i);
            if (isWinIntentDiscardCard) {
                discardCardFromHandToRulesPile(i);
                isWinIntentDiscardCard = false;
                return 2;
            }
        }
        for (int j = 0; j < hand.cards.size(); j++) {
            for (int i = 0; i < hand.cards.size(); i++) {
                if (i != j) {
                    tryToPlayThenDiscardCard(i, j);
                    if (isWinIntentDiscardCard && isWinIntentPlayCard) {
                        playCardFromHandToPalette(i);
                        discardCardFromHandToRulesPile(j);
                        isWinIntentPlayCard = false;
                        isWinIntentDiscardCard = false;
                        return 3;
                    }
                }
            }
        }
        isInGame = false;
        return 4;
    }

    @Override
    public String toString() {
        return "Player id = " + id + ":\n"
                + palette + "\n"
                + hand;
    }
}
