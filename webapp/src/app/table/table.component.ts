import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';
import {State} from "../state";
import {getCardsOnTable} from "../jass.selectors";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent {

  cardsOnTable$ = this.store.pipe(
    select(getCardsOnTable)
  )

  constructor(private store: Store<State>) {

  }
}
