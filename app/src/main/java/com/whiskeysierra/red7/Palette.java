package com.whiskeysierra.red7;

import java.util.ArrayList;

public class Palette {
    protected ArrayList<Card> cards = new ArrayList<>();

    protected void addCard(Card card) {
        cards.add(card);
    }

    protected void removeCard(Card card) {
        cards.remove(card);
    }

    protected void removeCard(int index) {
        cards.remove(index);
    }

    protected ArrayList<Card> getRuledCards(Card cardFromRulesPile) {
        ArrayList<Card> ruledCards = new ArrayList<>();

        switch (cardFromRulesPile.valueOfColor) {
            case 7:
                Card max = cards.get(0);
                for (Card card : cards) {
                    if (card.compareTo(max) > 0) max = card;
                }
                ruledCards.add(max);
                break;

            case 6:
                // Счет кол-ва карт одного номинала
                int[] countOneNumber = new int[] {0, 0, 0, 0, 0, 0, 0};
                for (Card card : cards) {
                    ++countOneNumber[card.number - 1];
                }

                // Выявление номинала с максимальным числом
                int maxCountOneNumber = 0;          // Максимальное кол-во карт одного номинала
                int numberOfMaxCountNumber = 0;     // Номинал этих карт
                for (int i = 0; i < 7; ++i) {
                    if (countOneNumber[i] >= maxCountOneNumber) {
                        maxCountOneNumber = countOneNumber[i];
                        numberOfMaxCountNumber = i + 1;
                    }
                }

                // Вывод всех карт соотв. номинала
                for (Card card : cards) {
                    if (card.number == numberOfMaxCountNumber) ruledCards.add(card);
                }
                break;

            case 5:
                // Счет кол-ва карт одного цвета
                int[] countOneColor = new int[] {0, 0, 0, 0, 0, 0, 0};
                int[] highestNumber = new int[] {0, 0, 0, 0, 0, 0, 0};
                for (Card card : cards) {
                    ++countOneColor[card.valueOfColor - 1];                 // +1 карта соотв. цвета
                    if (card.number > highestNumber[card.valueOfColor - 1])
                        highestNumber[card.valueOfColor - 1] = card.number; // сохр. номинал (если он старший в цвете)
                }

                // Выявление цвета с максимальным числом и старшей карты
                int maxCountOneColor = 0;           // Максимальное кол-во карт одного цвета
                int tempMaxNumber = 0;              // Старший номинал этого цвета
                int colorOfMaxCountColor = 0;       // Цвет этих карт

                for (int i = 0; i < 7; ++i) {
                    if      (countOneColor[i] > maxCountOneColor) {
                        maxCountOneColor = countOneColor[i];
                        colorOfMaxCountColor = i + 1;
                        tempMaxNumber = highestNumber[i];
                    }
                    else if (countOneColor[i] == maxCountOneColor &&
                            highestNumber[i] >= tempMaxNumber) {
                        maxCountOneColor = countOneColor[i];
                        tempMaxNumber = highestNumber[i];
                        colorOfMaxCountColor = i + 1;
                    }
                }

                // Вывод всех карт соотв. номинала
                for (Card card : cards) {
                    if (card.valueOfColor == colorOfMaxCountColor) ruledCards.add(card);
                }
                break;

            case 4:
                for (Card card : cards) {
                    if (card.number % 2 == 0) ruledCards.add(card);
                }
                break;

            case 3:
                int numberOfAddedCard;
                for (int color = 1; color <= 7; ++color) {
                    numberOfAddedCard = 0;
                    for (Card card : cards) {
                        if (card.valueOfColor == color) {
                            // Первая карта текущего цвета
                            if (numberOfAddedCard == 0) {
                                numberOfAddedCard = card.number;
                                ruledCards.add(card);
                            }
                            // Больше ли номинал этой карты, чем предыдущей
                            else if (card.number > numberOfAddedCard) {
                                numberOfAddedCard = card.number;
                                ruledCards.set(ruledCards.size() - 1, card);
                            }
                        }
                    }
                }
                break;

            case 2:
                int savedColor;
                boolean wasFound;
                ArrayList<Card> currentArray = new ArrayList<>();
                for (int i = 7; i >= 0; --i) {
                    savedColor = 0;
                    wasFound = false;

                    for (Card card : cards) {
                        if (card.number == i) {
                            if          (savedColor == 0) {
                                savedColor = card.valueOfColor;
                                currentArray.add(card);
                            } else if   (card.valueOfColor > savedColor) {
                                savedColor = card.valueOfColor;
                                currentArray.set(currentArray.size() - 1, card);
                            }
                            wasFound = true;
                        }
                    }

                    if (!wasFound) {
                        if (currentArray.size() > ruledCards.size()) {
                            ruledCards.clear();
                            ruledCards.addAll(currentArray);
                        }
                        currentArray.clear();
                    }
                }
                break;

            case 1:
                for (Card card : cards) if (card.number < 4) ruledCards.add(card);
                break;
        }
        return ruledCards;
    }

    @Override
    public String toString() {
        return "Palette:\n" + cards;
    }
}
