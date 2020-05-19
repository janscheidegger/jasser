import { Component, OnInit } from '@angular/core';
import { JassState } from '../jass.state';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

import { playCard } from '../jass.actions';
import { BackendService } from '../backend/backend.service';

@Component({
  selector: 'app-hand',
  templateUrl: './hand.component.html',
  styleUrls: ['./hand.component.scss'],
})
export class HandComponent implements OnInit {
  hand$: Observable<any[]>;

  constructor(private store: Store<{jass: JassState}>, private service: BackendService) {
  }

  ngOnInit(): void {
    console.log(this.store);
    this.hand$ = this.store.pipe(
      select(state => state.jass.hand),
      tap(s => console.log(s))
    );
  }

  playCard(card: any) {
    this.store.dispatch(playCard({ player: this.service.getUsername(), card }));
  }
}
