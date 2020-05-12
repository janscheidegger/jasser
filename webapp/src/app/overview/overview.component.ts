import { Component, OnInit } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Game } from '../backend/game';
import { BackendService } from '../backend/backend.service';
import { Router } from '@angular/router';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-overview',
  templateUrl: './overview.component.html',
  styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

  title = 'webapp';

  private readonly refreshToken$ = new BehaviorSubject(undefined);

  username: string;
  openGames$: Observable<Game[]>;

  constructor(private service: BackendService, private router: Router) {}
  ngOnInit(): void {
    this.openGames$ = this.refreshToken$.pipe(
      switchMap(() => this.service.listOpenGames())
    );
  }

  createGame() {
    this.service
      .createGame()
      .subscribe(() => this.refreshToken$.next(undefined));
  }

  startGame(gameId: string, username: string) {
    this.service.joinGame(username, gameId);
    this.router.navigate(['games', gameId]);
  }
}
