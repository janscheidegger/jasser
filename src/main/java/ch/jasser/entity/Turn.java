package ch.jasser.entity;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;

import java.util.HashMap;
import java.util.Map;

@BsonDiscriminator
public class Turn {

    private Map<Card, JassPlayer> cardsOnTable = new HashMap<>();

    public Turn() {
    }

    public void addCard(JassPlayer player, Card card) {
        this.cardsOnTable.put(card, player);
    }
}
