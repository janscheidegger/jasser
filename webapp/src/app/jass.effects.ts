import {Injectable} from '@angular/core';
import {BackendService} from './backend/backend.service';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
  errorReceived,
  gameLoaded,
  handOutCards,
  initialLoad,
  playCard,
  teamsChosen,
  trumpChosen,
  turnWon
} from './jass.actions';
import {of} from 'rxjs';
import {map, switchMap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Game} from "./backend/game";
import {MatDialog} from "@angular/material/dialog";

@Injectable()
export class JassEffects {

  playCard$ = createEffect(() => this.actions$.pipe(
    ofType(playCard),
    switchMap((action) => of(this.service.playCard(action.card)))
  ), {dispatch: false});

  createTeams$ = createEffect(() => this.actions$.pipe(
    ofType(teamsChosen),
    switchMap((action) => of(this.service.chooseTeams(action.teams)))
  ), {dispatch: false});

  reloadGame$ = createEffect(() => this.actions$.pipe(
    ofType(initialLoad),
    switchMap((action) => this.service.initialLoad(action.gameId).pipe(
      map((game: Game) => gameLoaded({game}))
      )
    )
  ));

  trumpChosen$ = createEffect(() => this.actions$.pipe(
    ofType(trumpChosen),
    switchMap(action => of(this.service.chooseTrump(action.suit)))
  ), {dispatch: false})

  showError$ = createEffect(() => this.actions$.pipe(
    ofType(errorReceived),
    map((action) =>
      this.snackBar.open(action.errorMessage, 'Error', {
        duration: 2500,
      })
    )
  ), {dispatch: false});

  turnWinner$ = createEffect(() => this.actions$.pipe(
    ofType(turnWon),
    map((action) =>
      this.snackBar.open(`${action.player} has won this turn`, 'MESSAGE', {
        duration: 2500,
      })
    )
  ), {dispatch: false});

  handOutCards$ = createEffect(() => this.actions$.pipe(
    ofType(handOutCards),
    switchMap(() => of(this.service.handOutCards()))
  ), {dispatch: false})

  constructor(
    private actions$: Actions,
    private service: BackendService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {
  }
}
