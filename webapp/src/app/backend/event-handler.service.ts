import {Injectable} from '@angular/core';
import {JassMessage} from './jass-message';
import {Store} from '@ngrx/store';
import {cardPlayed, cardsReceived, errorReceived, playerJoined, playerLeft,} from '../jass.actions';
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
        this.store.dispatch(cardsReceived({cards: ev.cards}));
        break;
      case 'PLAYER_JOINED':
        this.store.dispatch(playerJoined({player: ev.username}))
        break;
      case 'PLAYER_LEFT':
        this.store.dispatch(playerLeft({player: ev.username}))
        break;
      case 'ERROR':
        this.store.dispatch(errorReceived({errorMessage: ev.message}));
        break;
      case 'CARD_PLAYED':
        this.store.dispatch(
          cardPlayed({player: ev.username, card: ev.cards[0]})
        );
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
