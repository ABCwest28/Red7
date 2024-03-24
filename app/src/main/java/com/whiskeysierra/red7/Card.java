package com.whiskeysierra.red7;

public class Card implements Comparable<Card> {
    protected String color;
    protected int number;
    protected int valueOfColor;

    protected Card(String color, int number) {
        this.color = color;
        this.number = number;
        switch (color) {
            case "Red":
                this.valueOfColor = 7;
                break;
            case "Orange":
                this.valueOfColor = 6;
                break;
            case "Yellow":
                this.valueOfColor = 5;
                break;
            case "Green":
                this.valueOfColor = 4;
                break;
            case "Blue":
                this.valueOfColor = 3;
                break;
            case "Indigo":
                this.valueOfColor = 2;
                break;
            case "Violet":
                this.valueOfColor = 1;
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return number == card.number && valueOfColor == card.valueOfColor;
    }

    @Override
    public int hashCode() {
        return number * 7 + valueOfColor;
    }

    @Override
    public int compareTo(Card o) {
        if (this.number == o.number)
            return this.valueOfColor - o.valueOfColor;
        else    return this.number - o.number;
    }

    @Override
    public String toString() {
        return "[" + color + "|" + number + "]";
    }
}
