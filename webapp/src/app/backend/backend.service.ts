import { Injectable } from '@angular/core';
import { webSocket, WebSocketSubject } from 'rxjs/webSocket';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  private myWebSocket: WebSocketSubject<any>;


  constructor() {
    console.log('init ws');
    this.myWebSocket = webSocket('ws://localhost:8080/jass/jan');
    this.myWebSocket.subscribe();
  }

  getObservable() {
    return this.myWebSocket.asObservable();
  }

  startGame() {
    this.myWebSocket.next({event: 'start'});
  }


}
