package ch.jasser.entity;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

@BsonDiscriminator
public class Turn {

    private List<PlayedCard> cardsOnTable;

    public Turn() {
        this.cardsOnTable = new ArrayList<>();
    }

    @BsonCreator
    public Turn(@BsonProperty("cardsOnTable") List<PlayedCard> cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }


    public List<PlayedCard> getCardsOnTable() {
        return cardsOnTable;
    }

    public Suit getPlayedSuit() {
        if (cardsOnTable.size() > 0) {
            return cardsOnTable.get(0).getCard().getSuit();
        }
        return null;
    }
}
