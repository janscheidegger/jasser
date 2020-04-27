package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.InitialLoadResponse;
import ch.jasser.boundry.payload.PlayCardPayload;
import ch.jasser.entity.Card;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;

import javax.enterprise.context.Dependent;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Optional;

@Dependent
public class PlayCardAction implements Action<PlayCardPayload> {


    @Override
    public Optional<JassMessage> act(String payload) {
        Jsonb jsonb = JsonbBuilder.create();
        PlayCardPayload playCardPayload = jsonb.fromJson(payload, PlayCardPayload.class);

        return Optional.empty();
    }
}
