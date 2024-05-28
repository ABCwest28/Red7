package com.whiskeysierra.red7;

import java.util.ArrayList;

public class Game {
    protected ArrayList<Player> players;
    protected Deck deck;
    protected Card rulesPile;

    public Game(int numOfPlayers) {
        deck = new Deck();
        players = new ArrayList<>(numOfPlayers);
        rulesPile = new Card("Red", 0);

        for (int i = 0; i < numOfPlayers; ++i) {
            players.add(new Player(i, this));
        }
    }


    public int getIdWinner(Card ruleCard) {
        class DataToCheck {
            int id;
            ArrayList<Card> cards;
            boolean isExcluded;

            DataToCheck(int id, ArrayList<Card> cards) {
                this.id = id;
                this.cards = cards;
                isExcluded = false;
            }
        }
        ArrayList<DataToCheck> listDataToChecks = new ArrayList<>();

        for (int i = 0; i < players.size(); ++i) {
            listDataToChecks.add(new DataToCheck(i, players.get(i).getRuledCards(ruleCard)));
        }

        // Проверяем по кол-ву карт - исключаем у кого меньше карт
        int maxCount = 0;
        for (DataToCheck dataToCheck : listDataToChecks)
            if (dataToCheck.cards.size() > maxCount) maxCount = dataToCheck.cards.size();
        for (DataToCheck dataToCheck : listDataToChecks)
            if (dataToCheck.cards.size() < maxCount) dataToCheck.isExcluded = true;


        int maxHashCode = 0;
        int idOfPlayerWithHighestCard = 0;
        for (int i = 0; i < listDataToChecks.size(); ++i) {
            if (!listDataToChecks.get(i).isExcluded) {
                for (Card card : listDataToChecks.get(i).cards) {
                    if (card.hashCode() > maxHashCode) {
                        maxHashCode = card.hashCode();
                        idOfPlayerWithHighestCard = i;
                    }
                }
            }
        }

        return idOfPlayerWithHighestCard;
    }


    public boolean checkWin() {
        int activePlayers = 0;
        for (Player player : players) if (player.isInGame) ++activePlayers;
        if (activePlayers == 1)
            return true;
        else
            return false;
    }


    public int getNextId() {
        int id = getIdWinner(rulesPile);
        do {
            if (id + 1 < players.size()) id++;
            else id = 0;
            if (players.get(id).isInGame) return id;
        } while (true);
    }
}
