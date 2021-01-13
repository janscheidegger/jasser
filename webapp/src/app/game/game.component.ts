import { Component, OnInit, OnDestroy } from '@angular/core';
import { BackendService } from '../backend/backend.service';
import { ActivatedRoute } from '@angular/router';
import { combineLatest, Subject, of } from 'rxjs';
import { takeUntil, switchMap } from 'rxjs/operators';
import {Store} from "@ngrx/store";
import {JassState} from "../jass.state";
import {initialLoad} from "../jass.actions";

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit, OnDestroy {
  destroy$: Subject<any> = new Subject();

  constructor(private store: Store<{jass: JassState}>, private service: BackendService, private route: ActivatedRoute) {
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

}
