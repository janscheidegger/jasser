import { Component, OnInit } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'webapp';

  private backendObservable: Observable<any>;

constructor(private service: BackendService) {}

  ngOnInit(): void {
    this.backendObservable = this.service.getObservable();
    this.backendObservable.subscribe( v => console.log(v), e => console.error(e), () => console.log('complete') );
  }

startGame() {
    this.service.startGame();
  }

}
