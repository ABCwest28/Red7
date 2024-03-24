package com.whiskeysierra.red7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    private ArrayList<Card> cards;
    protected int numOfCards = 49;
    private final Random random = new Random();

    protected Deck() {
        String[] colors = {"Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet"};
        cards = new ArrayList<>();
        for (int i = 1; i <= 7; ++i) {
            for (int j = 0; j < 7; ++j) {
                cards.add(new Card(colors[j], i));
            }
        }
        shuffle();
    }

    private void shuffle() {
        Collections.shuffle(cards);
    }

    protected Card returnCard() {
        return cards.remove(random.nextInt(numOfCards--));
    }

    protected void setCardOnTop(Card card) {
        cards.add(card);
        ++numOfCards;
    }

    @Override
    public String toString() {
        return "Deck:\n" + cards;
    }
}
