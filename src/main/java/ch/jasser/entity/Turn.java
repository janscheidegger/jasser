package ch.jasser.entity;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

@BsonDiscriminator
public class Turn {

    private List<PlayedCard> cardsOnTable;
    private Suit playedSuit;

    public Turn() {
        this.cardsOnTable = new ArrayList<>();
        this.playedSuit = null;
    }

    @BsonCreator
    public Turn(@BsonProperty("cardsOnTable") List<PlayedCard> cardsOnTable, @BsonProperty("playedSuit") Suit playedSuit) {
        this.cardsOnTable = cardsOnTable;
        this.playedSuit = playedSuit;
    }


    public List<PlayedCard> getCardsOnTable() {
        return cardsOnTable;
    }

    public Suit getPlayedSuit() {
        return playedSuit;
    }
}
