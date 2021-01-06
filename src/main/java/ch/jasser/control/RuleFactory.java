package ch.jasser.control;

import ch.jasser.control.actions.Action;
import ch.jasser.control.actions.ChoosePartnerAction;
import ch.jasser.control.actions.ChooseTrumpAction;
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
    private final Action chooseTrumpAction;
    private final Action choosePartnerAction;

    RuleFactory(PlayCardAction playCardAction, HandOutCardsAction handOutCardsAction, ChooseTrumpAction chooseTrumpAction, ChoosePartnerAction choosePartnerAction) {
        this.playCardAction = playCardAction;
        this.handOutCardsAction = handOutCardsAction;
        this.chooseTrumpAction = chooseTrumpAction;
        this.choosePartnerAction = choosePartnerAction;
    }

    @Produces
    @ApplicationScoped
    Rules createRules() {
        Rules rules = new Schieber();
        rules.registerAction(GameStep.MOVE, playCardAction);
        rules.registerAction(GameStep.HAND_OUT, handOutCardsAction);
        rules.registerAction(GameStep.CHOOSE_TRUMP, chooseTrumpAction);
        rules.registerAction(GameStep.CHOOSE_PARTNER, choosePartnerAction);
        return rules;
    }

}
