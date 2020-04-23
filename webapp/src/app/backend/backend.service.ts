import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Game } from './game';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private currentGameConnection: WebSocketSubject<any>;

  constructor(private http: HttpClient) {}

  createNewGame(username: string): Observable<Game> {
    return this.http.get<Game>(`http://localhost:8080/jass/${username}`);
  }

  initialLoad() {
    this.currentGameConnection.next({event: "INITIAL_LOAD"});
  }

  startGame(username: string, gameId: string): Observable<any> {
    this.currentGameConnection = webSocket(
      `ws://localhost:8080/jass/${username}/${gameId}`
    );
    this.currentGameConnection.subscribe();
    return this.currentGameConnection.asObservable();
  }
}
