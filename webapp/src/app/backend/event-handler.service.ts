import {Injectable} from '@angular/core';
import {JassMessage} from './jass-message';
import {Store} from '@ngrx/store';
import {
  cardPlayed,
  cardsReceived,
  chooseTrump,
  errorReceived,
  playerJoined,
  playerLeft, roundOver,
  trumpChosen, trumpSelectionReceived, turnWon,
} from '../jass.actions';
import {State} from "../jass.reducer";

@Injectable({
  providedIn: 'root',
})
export class EventHandlerService {
  constructor(private store: Store<State>) {
  }

  public handleEvent(ev: JassMessage) {
    console.log(`received message`, ev);
    switch (ev.event) {
      case 'RECEIVE_CARD':
        console.log(ev);
        this.store.dispatch(cardsReceived({nextStep: ev.nextStep, cards: ev.cards}));
        break;
      case 'PLAYER_JOINED':
        this.store.dispatch(playerJoined({nextStep: ev.nextStep, player: ev.username}))
        break;
      case 'PLAYER_LEFT':
        this.store.dispatch(playerLeft({nextStep: ev.nextStep, player: ev.username}))
        break;
      case 'ERROR':
        this.store.dispatch(errorReceived({nextStep: ev.nextStep, errorMessage: ev.message}));
        break;
      case 'CHOOSE_TRUMP':
        this.store.dispatch(chooseTrump({nextStep: ev.nextStep}));
        break;
      case 'CARD_PLAYED':
        this.store.dispatch(
          cardPlayed({nextStep: ev.nextStep, player: ev.username, card: ev.cards[0]})
        );
        break;
      case 'TURN_WON':
        this.store.dispatch(turnWon({nextStep: ev.nextStep, player: ev.username}));
        break;
      case 'TRUMP_CHOSEN':
        this.store.dispatch(
          trumpSelectionReceived({nextStep: ev.nextStep, suit: ev.chosenTrump})
        )
        break;
      case 'ROUND_OVER':
        this.store.dispatch(roundOver({nextStep: ev.nextStep, teams: ev.teams}));
        break;
      default:
        console.log(`Unhandled Action ${ev.event}`);
    }
  }

  public onError(err: any) {
  }

  public onComplete() {
  }
}
