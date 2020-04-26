import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { JassState } from '../jass.state';
import { Store, select } from '@ngrx/store';

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent {

  cardsOnTable$: Observable<any[]>;

  constructor(private store: Store<{jass: JassState}>) {
    this.cardsOnTable$ = store.pipe(select(state => state.jass.table));
  }
}
