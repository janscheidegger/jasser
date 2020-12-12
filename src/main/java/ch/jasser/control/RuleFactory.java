package ch.jasser.control;

import ch.jasser.control.actions.Action;
import ch.jasser.control.actions.HandOutCardsAction;
import ch.jasser.control.actions.PlayCardAction;
import ch.jasser.control.gamerules.Rules;
import ch.jasser.control.gamerules.schieber.Schieber;
import ch.jasser.control.steps.GameStep;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class RuleFactory {

    private final Action playCardAction;
    private final Action handOutCardsAction;

    RuleFactory(PlayCardAction playCardAction, HandOutCardsAction handOutCardsAction) {
        this.playCardAction = playCardAction;
        this.handOutCardsAction = handOutCardsAction;
    }

    @Produces
    @ApplicationScoped
    Rules createRules() {
        Rules rules = new Schieber();
        rules.registerAction(GameStep.MOVE, playCardAction);
        rules.registerAction(GameStep.PRE_ROUND, handOutCardsAction);
        return rules;
    }

}
