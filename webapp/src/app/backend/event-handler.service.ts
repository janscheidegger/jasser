import { Injectable } from '@angular/core';
import { JassMessage } from './jass-message';
import { Store } from '@ngrx/store';
import { JassState } from '../jass.state';
import { initialLoad, receiveCard, errorReceived } from '../jass.actions';

@Injectable({
  providedIn: 'root',
})
export class EventHandlerService {
  constructor(private store: Store<{ jass: JassState }>) {}

  public handleEvent(ev: JassMessage) {
    const response: any = JSON.parse(ev.payloadString);
    switch (ev.event) {
      case 'INITIAL_LOAD':
        this.store.dispatch(initialLoad(response));
        break;
      case 'RECEIVE_CARD':
        console.log(response);
        this.store.dispatch(receiveCard({ card: response }));
        break;
      case 'ERROR':
        this.store.dispatch(errorReceived({ errorMessage: response }));
        break;
      default:
        console.log('Unhandled Action');
        console.log(response);
    }
  }

  public onError(err: any) {}

  public onComplete() {}
}
