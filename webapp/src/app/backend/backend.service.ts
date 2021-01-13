import {Injectable} from '@angular/core';
import {webSocket, WebSocketSubject} from 'rxjs/webSocket';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Game} from './game';
import {Card} from './card';
import {EventHandlerService} from './event-handler.service';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private currentGameConnection: WebSocketSubject<any>;
  private username: string;
  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
    }),
  };

  constructor(
    private http: HttpClient,
    private eventHandler: EventHandlerService
  ) {
  }

  listOpenGames(): Observable<Game[]> {

    return this.http.get<Game[]>(`http://localhost:8080/jass/games`);
  }

  createGame() {
    return this.http.post<Game>(
      `http://localhost:8080/jass/games`,
      '{}',
      this.httpOptions
    );
  }

  playCard(card: Card) {
    return this.currentGameConnection.next({
      event: 'PLAY_CARD',
      payloadString: JSON.stringify({card}),
    });
  }

  initialLoad(gameId: string): Observable<Game> {
    return this.http.get<Game>(`http://localhost:8080/jass/games/${gameId}`);
  }

  handOutCards() {
    this.currentGameConnection.next({event: 'HAND_OUT_CARDS'});
  }

  joinGame(username: string, gameId: string): void {
    this.currentGameConnection = webSocket(
      `ws://localhost:8080/jass/${username}/${gameId}`
    );
    this.username = username;
    this.currentGameConnection.subscribe(
      (ev) => this.eventHandler.handleEvent(ev),
      (err) => this.eventHandler.onError(err),
      () => this.eventHandler.onComplete
    );
  }

  getUsername(): string {
    return this.username;
  }
}
