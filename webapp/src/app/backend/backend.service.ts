import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Game } from './game';

@Injectable({
  providedIn: 'root',
})
export class BackendService {
  private currentGameConnection: WebSocketSubject<any>;

  private httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':  'application/json'
    })
  };

  constructor(private http: HttpClient) {}

  listOpenGames(username: string): Observable<Game[]> {
    return this.http.get<Game[]>(`http://localhost:8080/jass/${username}/games`);
  }

  createGame(username: string) {
    return this.http.post<Game>(`http://localhost:8080/jass/${username}/games`, '{}', this.httpOptions);
  }

  initialLoad() {
    this.currentGameConnection.next({event: "INITIAL_LOAD"});
  }

  joinGame(username: string, gameId: string): Observable<any> {
    this.currentGameConnection = webSocket(
      `ws://localhost:8080/jass/${username}/${gameId}`
    );
    this.currentGameConnection.subscribe();
    return this.currentGameConnection.asObservable();
  }
}
