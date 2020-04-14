package ch.jasser.entity;

public enum Rank {
    ACE(9), KING(8), QUEEN(7), JACK(6), TEN(5), NINE(4), EIGHT(4), SEVEN(3), SIX(1);

    private final int value;
    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
