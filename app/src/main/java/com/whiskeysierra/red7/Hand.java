package com.whiskeysierra.red7;

import java.util.ArrayList;

public class Hand {
    protected ArrayList<Card> cards = new ArrayList<>(7);

    protected void addCard(Card card) {
        cards.add(card);
    }

    protected void removeCard(Card card) {
        cards.remove(card);
    }

    protected void removeCard(int index) {
        cards.remove(index);
    }

    @Override
    public String toString() {
        return "Hand:\n" + cards;
    }
}
