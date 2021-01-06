package ch.jasser.entity;

public enum Rank {
    ACE(9, 11, 11),
    KING(8, 4, 4),
    QUEEN(7, 3, 3),
    JACK(6, 2, 20),
    TEN(5, 10, 10),
    NINE(4, 0, 14),
    EIGHT(4, 0, 0),
    SEVEN(3, 0, 0),
    SIX(1, 0, 0);

    private final int value;
    private final int points;
    private final int trumpPoints;

    Rank(int value, int points, int trumpPoints) {
        this.value = value;
        this.points = points;
        this.trumpPoints = trumpPoints;
    }

    public int getValue() {
        return value;
    }

    public int getPoints(boolean trump) {
        return trump ? trumpPoints : points;
    }
}
