import { Component, OnInit, Input } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent {
  title = 'webapp';

  private backendObservable: Observable<any>;

  username: string;

  constructor(private service: BackendService) {}

  initialLoad() {
    this.service.initialLoad();
  }

  startGame() {
    this.service.createNewGame('jan').subscribe((r) => {
      this.backendObservable = this.service.startGame('jan', r.gameId);
      this.backendObservable.subscribe(
        (v) => console.log(v),
        (e) => console.error(e),
        () => console.log('complete')
      );
    });
  }
}
