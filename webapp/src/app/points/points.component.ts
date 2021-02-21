import { Component, OnInit } from '@angular/core';
import {select, Store} from "@ngrx/store";
import {State} from "../state";
import {getJassState, pointsPerTeam} from "../jass.selectors";

@Component({
  selector: 'app-points',
  templateUrl: './points.component.html',
  styleUrls: ['./points.component.scss']
})
export class PointsComponent implements OnInit {

  constructor(private store: Store<State>) { }

  points$: { team: string, points: number; }[] = this.store.pipe(select(pointsPerTeam));

  ngOnInit(): void {
  }

}
