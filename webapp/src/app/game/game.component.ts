import { Component, OnInit, OnDestroy } from '@angular/core';
import { BackendService } from '../backend/backend.service';
import { ActivatedRoute } from '@angular/router';
import {combineLatest, Subject, of, Observable} from 'rxjs';
import {takeUntil, switchMap, tap} from 'rxjs/operators';
import {select, Store} from "@ngrx/store";
import {initialLoad} from "../jass.actions";
import {Player} from "../backend/player";
import {getPlayers} from "../jass.selectors";
import {State} from "../state";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  destroy$: Subject<any> = new Subject();
  players$: Observable<string[]> = this.store.pipe(
    tap(console.log),
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
    this.route.params.pipe(

    );
  }

  initialLoad() {
  }

  handOutCards() {
    this.service.handOutCards();
  }

  reload() {
    // this.service.initialLoad(this.)
  }
}
