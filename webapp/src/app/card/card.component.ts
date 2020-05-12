import { Component, OnInit, Input } from '@angular/core';
import { Card } from '../backend/card';

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.scss'],
})
export class CardComponent implements OnInit {
  @Input() card: Card;

  constructor() {}

  ngOnInit(): void {}

  numValue(card: Card): number {
    switch (card.rank) {
      case 'SIX':
        return 6;
      case 'SEVEN':
        return 7;
      case 'EIGHT':
        return 8;
      case 'NINE':
        return 9;
      case 'TEN':
        return 10;
    }
  }

  suitImage(card: Card): string {
    switch (card.suit) {
      case 'HEARTS': return '♥';
      case 'SPADES': return '♠';
    }
  }
}
