import { Injectable } from '@angular/core';
import { JassMessage } from './jass-message';
import { Store } from '@ngrx/store';
import { JassState } from '../jass.state';
import { initialLoad } from '../jass.actions';
import { Card } from './card';

@Injectable({
  providedIn: 'root',
})
export class EventHandlerService {
  constructor(private store: Store<{ jass: JassState }>) {}

  public handleEvent(ev: JassMessage) {
    const response: { cards: Card[] } = JSON.parse(ev.payloadString);
    switch (ev.event) {
      case 'INITIAL_LOAD':
        this.store.dispatch(initialLoad(response));
    }
  }

  public onError(err: any) {}

  public onComplete() {}
}
