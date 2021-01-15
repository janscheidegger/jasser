import {Injectable} from '@angular/core';
import {BackendService} from './backend/backend.service';
import {Actions, Effect, ofType} from '@ngrx/effects';
import {errorReceived, gameLoaded, initialLoad, playCard} from './jass.actions';
import {of} from 'rxjs';
import {map, switchMap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Game} from "./backend/game";

@Injectable()
export class JassEffects {
  @Effect({dispatch: false})
  playCard$ = this.actions$.pipe(
    ofType(playCard),
    switchMap((action) => of(this.service.playCard(action.card)))
  );
  @Effect({dispatch: true})
  reloadGame$ = this.actions$.pipe(
    ofType(initialLoad),
    switchMap((action) => this.service.initialLoad(action.gameId).pipe(
      map((game: Game) => gameLoaded({game}))
      )
    )
  );

  @Effect({dispatch: false})
  showError$ = this.actions$.pipe(
    ofType(errorReceived),
    map((action) =>
      this.snackBar.open(action.errorMessage, 'Error', {
        duration: 2500,
      })
    )
  );

  constructor(
    private actions$: Actions,
    private service: BackendService,
    private snackBar: MatSnackBar
  ) {
  }
}
