package ch.jasser.control;

import ch.jasser.entity.Card;
import ch.jasser.entity.Game;
import ch.jasser.entity.JassPlayer;
import ch.jasser.entity.PlayedCard;
import ch.jasser.entity.Suit;
import ch.jasser.entity.Team;
import ch.jasser.entity.Turn;
import io.quarkus.test.Mock;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mock
@Priority(1)
@ApplicationScoped
public class GamesRepositoryTestMock extends GamesRepositoryMock {


}
