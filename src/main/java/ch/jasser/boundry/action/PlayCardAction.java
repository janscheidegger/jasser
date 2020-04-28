package ch.jasser.boundry.action;

import ch.jasser.boundry.JassMessage;
import ch.jasser.boundry.payload.InitialLoadResponse;
import ch.jasser.boundry.payload.PlayCardPayload;
import ch.jasser.control.GameCoordinator;
import ch.jasser.entity.Card;
import ch.jasser.entity.Rank;
import ch.jasser.entity.Suit;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Optional;

@Dependent
public class PlayCardAction implements Action<PlayCardPayload> {

    private GameCoordinator gameCoordinator;

    public PlayCardAction(GameCoordinator gameCoordinator) {
        this.gameCoordinator = gameCoordinator;
    }

    @Override
    public Optional<JassMessage> act(String username, String gameId, String payload) {
        Jsonb jsonb = JsonbBuilder.create();
        PlayCardPayload playCardPayload = jsonb.fromJson(payload, PlayCardPayload.class);

        System.out.println(String.format("[%s]%s played %s", gameId, username, playCardPayload));

        this.gameCoordinator.playCard(username, gameId, playCardPayload.getCard());

        return Optional.empty();
    }
}
