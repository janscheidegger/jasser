import {Component, OnDestroy, OnInit} from '@angular/core';
import {BackendService} from '../backend/backend.service';
import {ActivatedRoute} from '@angular/router';
import {combineLatest, Observable, of, Subject} from 'rxjs';
import {map, startWith, switchMap, takeUntil, tap} from 'rxjs/operators';
import {select, Store} from "@ngrx/store";
import {getPlayers} from "../jass.selectors";
import {State} from "../state";
import {initialLoad} from "../jass.actions";

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
  players$: Observable<string[]> = this.store.pipe(
    select(getPlayers),
    tap(console.log)
  )


  constructor(private store: Store<State>, private service: BackendService, private route: ActivatedRoute) {
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
    combineLatest([this.gameId$, this.reload$.pipe(startWith(''))]).subscribe(([gameId, _]) =>
      this.store.dispatch(initialLoad({gameId}))
    )
  }

  initialLoad() {
  }

  handOutCards() {
    this.service.handOutCards();
  }

  reload() {
    this.reload$.next();
  }
}
