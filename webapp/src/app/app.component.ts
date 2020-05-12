import { Component, OnInit, Input } from '@angular/core';
import { BackendService } from './backend/backend.service';
import { Observable, BehaviorSubject } from 'rxjs';
import { Game } from './backend/game';
import { switchMap } from 'rxjs/operators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
})
export class AppComponent  {

}
