import { Injectable } from '@angular/core';
import { JassMessage } from './jass-message';
import { Store } from '@ngrx/store';
import { JassState } from '../jass.state';
import {
  initialLoad,
  cardReceived,
  errorReceived,
  cardPlayed, playerJoined,
} from '../jass.actions';

@Injectable({
  providedIn: 'root',
})
export class EventHandlerService {
  constructor(private store: Store<{ jass: JassState }>) {}

  public handleEvent(ev: JassMessage) {
    console.log(`received message`,  ev);
    switch (ev.event) {
      case 'RECEIVE_CARD':
        console.log(ev);
        this.store.dispatch(cardReceived({ card: ev.cards[0] }));
        break;
      case 'PLAYER_JOINED':
        this.store.dispatch(playerJoined({player: ev.username}))
        break;
      case 'ERROR':
        this.store.dispatch(errorReceived({errorMessage: ev.message}));
        break;
      case 'CARD_PLAYED':
        this.store.dispatch(
          cardPlayed({ player: ev.username, card: ev.cards[0] })
        );
        break;
      default:
        console.log(`Unhandled Action ${ev.event}`);
    }
  }

  public onError(err: any) {}

  public onComplete() {}
}
