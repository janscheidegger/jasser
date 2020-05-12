import { Component, OnInit } from '@angular/core';
import { BackendService } from '../backend/backend.service';

@Component({
  selector: 'app-game',
  templateUrl: './game.component.html',
  styleUrls: ['./game.component.scss']
})
export class GameComponent implements OnInit {

  constructor(private service: BackendService) { }

  ngOnInit(): void {
  }

  initialLoad() {
    this.service.initialLoad();
  }

  handOutCards() {
    this.service.handOutCards();
  }

}
