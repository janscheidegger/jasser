package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.EmptyPayload;
import ch.jasser.boundry.payload.InitialLoadResponse;
import ch.jasser.entity.Card;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Optional;

@Dependent
public class InitialLoadAction implements Action<EmptyPayload> {

    @Override
    public Optional<JassMessage> act(String username, String gameId, String payload) {
        Jsonb jsonb = JsonbBuilder.create();
        InitialLoadResponse initialLoadResponse = new InitialLoadResponse();
        initialLoadResponse.addCard(new Card(Rank.ACE, Suit.DIAMONDS));
        initialLoadResponse.addCard(new Card(Rank.NINE, Suit.HEARTS));

        JassMessage response = new JassMessage();
        response.setEvent(EventType.INITIAL_LOAD);
        response.setPayloadString(jsonb.toJson(initialLoadResponse));
        System.out.println("INITIALLOAD: "+response);
        return Optional.of(response);
    }
}
