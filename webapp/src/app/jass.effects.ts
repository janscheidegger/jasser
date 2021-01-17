import {Injectable} from '@angular/core';
import {BackendService} from './backend/backend.service';
import {Actions, createEffect, Effect, ofType} from '@ngrx/effects';
import {errorReceived, gameLoaded, initialLoad, playCard, teamsChosen} from './jass.actions';
import {of} from 'rxjs';
import {map, switchMap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Game} from "./backend/game";

@Injectable()
export class JassEffects {

  playCard$ = createEffect(() => this.actions$.pipe(
    ofType(teamsChosen),
    switchMap((action) => of(this.service.chooseTeams(action.teams)))
  ), {dispatch: false});

  createTeams$ = createEffect(() => this.actions$.pipe(
    ofType()
  ))

  reloadGame$ = createEffect(() => this.actions$.pipe(
    ofType(initialLoad),
    switchMap((action) => this.service.initialLoad(action.gameId).pipe(
      map((game: Game) => gameLoaded({game}))
      )
    )
  ));

  showError$ = createEffect(() => this.actions$.pipe(
    ofType(errorReceived),
    map((action) =>
      this.snackBar.open(action.errorMessage, 'Error', {
        duration: 2500,
      })
    )
  ), {dispatch: false});

  constructor(
    private actions$: Actions,
    private service: BackendService,
    private snackBar: MatSnackBar
  ) {
  }
}
