import {Component, OnDestroy, OnInit} from '@angular/core';
import {BackendService} from '../backend/backend.service';
import {ActivatedRoute} from '@angular/router';
import {combineLatest, Observable, of, Subject} from 'rxjs';
import {map, startWith, switchMap, takeUntil, tap} from 'rxjs/operators';
import {select, Store} from "@ngrx/store";
import {getPlayers} from "../jass.selectors";
import {State} from "../state";
import {initialLoad, teamsChosen} from "../jass.actions";
import {MatDialog} from "@angular/material/dialog";
import {CreateTeamComponent} from "../create-team/create-team.component";

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

  handOutCards() {
    this.service.handOutCards();
  }

  reload() {
    this.reload$.next();
  }
}
