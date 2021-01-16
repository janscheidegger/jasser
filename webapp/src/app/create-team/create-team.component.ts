import {Component} from '@angular/core';
import {State} from "../state";
import {select, Store} from "@ngrx/store";
import {Observable} from "rxjs";
import {getPlayers} from "../jass.selectors";
import {Team} from "../team";
import {MatOptionSelectionChange} from "@angular/material/core";

@Component({
  selector: 'app-create-team',
  templateUrl: './create-team.component.html',
  styleUrls: ['./create-team.component.scss']
})
export class CreateTeamComponent {

  players$: Observable<string[]> = this.store.pipe(
    select(getPlayers)
  );

  teams: Team[] = [];

  constructor(private store: Store<State>) {
  }


  addTeam(teamName: string) {
    this.teams.push({name: teamName, players: []});
  }

  onSelectionChange($event: MatOptionSelectionChange, player: string) {
    if ($event.isUserInput) {
      for (const team of this.teams) {
        team.players = team.players.filter(p => p !== player)
      }
      $event.source.value.players = [...$event.source.value.players, player];
    }
  }
}
