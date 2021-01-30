import {Injectable} from '@angular/core';
import {BackendService} from './backend/backend.service';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {
  chooseTrump,
  errorReceived,
  gameLoaded,
  handOutCards,
  initialLoad,
  playCard,
  teamsChosen,
  trumpChosen
} from './jass.actions';
import {of} from 'rxjs';
import {exhaustMap, map, startWith, switchMap} from 'rxjs/operators';
import {MatSnackBar} from '@angular/material/snack-bar';
import {Game} from "./backend/game";
import {MatDialog} from "@angular/material/dialog";
import {CreateTeamComponent} from "./create-team/create-team.component";

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

  chooseTrump$ = createEffect(() => this.actions$.pipe(
    ofType(chooseTrump),
    exhaustMap(_ => {
      let dialogRef = this.dialog.open(CreateTeamComponent);
      return dialogRef.afterClosed();
    }),
    map(result => console.log(result))
  ), {dispatch: false});

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
