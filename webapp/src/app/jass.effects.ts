import { Injectable } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Actions, ofType, createEffect } from '@ngrx/effects';
import { playCard, cardPlayed } from './jass.actions';
import { of } from 'rxjs';
import { exhaustMap, map } from 'rxjs/operators';

@Injectable()
export class JassEffects {
  constructor(private actions$: Actions, private service: BackendService) {}

  playCard$ = createEffect(() =>
    this.actions$.pipe(
      ofType(playCard),
      exhaustMap((action) =>
        of(this.service.playCard(action.card)).pipe(map(() => cardPlayed))
      )
    )
  );
}
