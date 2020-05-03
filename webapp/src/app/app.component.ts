import { Component, OnInit, Input } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { Game } from './backend/game';
import { switchMap } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'webapp';

  private readonly refreshToken$ = new BehaviorSubject(undefined);

  username: string;
  openGames$: Observable<Game[]>;

  constructor(private service: BackendService) {}
  ngOnInit(): void {
    this.openGames$ = this.refreshToken$.pipe(
      switchMap(() => this.service.listOpenGames())
    );
  }

  initialLoad() {
    this.service.initialLoad();
  }

  createGame() {
    this.service
      .createGame()
      .subscribe(() => this.refreshToken$.next(undefined));
  }

  startGame(gameId: string, username: string) {
    this.service.joinGame(username, gameId);
  }

  handOutCards() {
    this.service.handOutCards();
  }
}
