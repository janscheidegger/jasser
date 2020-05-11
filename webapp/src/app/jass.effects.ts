import { Injectable } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Actions, ofType, createEffect, Effect } from '@ngrx/effects';
import { playCard, cardPlayed } from './jass.actions';
import { of } from 'rxjs';
import { exhaustMap, map, switchMap } from 'rxjs/operators';

@Injectable()
export class JassEffects {
  constructor(private actions$: Actions, private service: BackendService) {}

  @Effect()
  playCard$ = this.actions$.pipe(
    ofType(playCard),
    switchMap((action) =>
      of(this.service.playCard(action.card)).pipe(map(() => cardPlayed()))
    )
  );
}
