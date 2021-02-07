import {Component, OnDestroy, OnInit} from '@angular/core';
import {BackendService} from '../backend/backend.service';
import {ActivatedRoute} from '@angular/router';
import {combineLatest, Observable, of, Subject} from 'rxjs';
import {map, startWith, switchMap, takeUntil, tap} from 'rxjs/operators';
import {select, Store} from "@ngrx/store";
import {canSelectTrump, currentState, getPlayers} from "../jass.selectors";
import {State} from "../state";
import {initialLoad, teamsChosen, handOutCards, trumpChosen, schieben} from "../jass.actions";
import {MatDialog} from "@angular/material/dialog";
import {CreateTeamComponent} from "../create-team/create-team.component";
import {ChooseTrumpComponent} from "../choose-trump/choose-trump.component";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  destroy$: Subject<any> = new Subject();
  reload$: Subject<any> = new Subject<any>()
  gameId$: Observable<string> = this.route.paramMap.pipe(
    map(params => params.get('gameId'))
  );
  name$: Observable<string> = this.route.paramMap.pipe(
    map(params => params.get('username'))
  );
  players$: Observable<string[]> = this.store.pipe(
    select(getPlayers),
  )

  canSelectTrump$: Observable<boolean> = this.store.pipe(
    select(canSelectTrump)
  );

  currentState$ = this.store.pipe(
    select(currentState)
  );

  constructor(private store: Store<State>, private service: BackendService, private route: ActivatedRoute, private dialog: MatDialog) {
    this.route.params.pipe(
      switchMap(p => of(this.service.joinGame(p.username, p.gameId))),
      takeUntil(this.destroy$)
    ).subscribe(
      v => console.log(v),
      e => console.error(e),
      () => console.log('completed')
    );
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }


  ngOnInit(): void {
    combineLatest([this.gameId$, this.name$, this.reload$.pipe(startWith(''))]).pipe(
      takeUntil(this.destroy$)
    ).subscribe(([gameId, name, _]) =>
      this.store.dispatch(initialLoad({gameId, name}))
    );
  }

  chooseTeams() {
    const dialogRef = this.dialog.open(CreateTeamComponent);
    dialogRef.afterClosed().subscribe(result => this.store.dispatch(teamsChosen({teams: result})))
  }

  chooseTrump() {
    const dialogRef = this.dialog.open(ChooseTrumpComponent);
    dialogRef.afterClosed().subscribe(result =>  {
      console.log(result);
      if(result === 'SCHIEBEN') {
        console.log("SCHIEIIIIEBEN");
        this.store.dispatch(schieben());
      }
      else if (['HEARTS', 'SPADES', 'DIAMONDS', 'CLUBS'].includes(result)) {
        this.store.dispatch(trumpChosen({suit: result}))
      }
    })
  }

  handOutCardsToAll() {
    console.debug("Hand out Cards");
    this.store.dispatch(handOutCards());
  }

  reload() {
    this.reload$.next();
  }
}
