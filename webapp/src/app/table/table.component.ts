import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';
import {State} from "../jass.reducer";

@Component({
  selector: 'app-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss']
})
export class TableComponent {

  cardsOnTable$: Observable<any[]>;

  constructor(private store: Store<State>) {

  }
}
