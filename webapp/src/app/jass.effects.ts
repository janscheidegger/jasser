import { Injectable } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Actions, ofType, createEffect, Effect } from '@ngrx/effects';
import { playCard, cardPlayed, errorReceived } from './jass.actions';
import { of } from 'rxjs';
import { exhaustMap, map, switchMap } from 'rxjs/operators';
import { dispatch } from 'rxjs/internal/observable/pairs';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class JassEffects {
  constructor(
    private actions$: Actions,
    private service: BackendService,
    private snackBar: MatSnackBar
  ) {}

  @Effect({ dispatch: false })
  playCard$ = this.actions$.pipe(
    ofType(playCard),
    switchMap((action) => of(this.service.playCard(action.card)))
  );

  @Effect({ dispatch: false })
  showError$ = this.actions$.pipe(
    ofType(errorReceived),
    map((action) =>
      this.snackBar.open(action.errorMessage, 'Error', {
        duration: 2500,
      })
    )
  );
}
