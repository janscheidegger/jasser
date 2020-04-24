import { Component, OnInit, Input } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Observable } from 'rxjs';
import { Game } from './backend/game';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent implements OnInit {
  title = 'webapp';

  private backendObservable: Observable<any>;

  username: string;
  openGames$: Observable<Game[]>;

  constructor(private service: BackendService) {}
  ngOnInit(): void {
    this.openGames$ = this.service.listOpenGames('jan');
  }

  initialLoad() {
    this.service.initialLoad();
  }

  createGame() {
    this.service.createGame('jan').subscribe();
  }

  startGame(gameId: string) {
    this.backendObservable = this.service.joinGame('jan', gameId);
    this.backendObservable.subscribe(
      (v) => console.log(v),
      (e) => console.error(e),
      () => console.log('complete')
    );
  }
}
