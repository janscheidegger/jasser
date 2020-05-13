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
      case 'ACE':
        return 1;
      case 'KING':
        return 2;
      case 'QUEEN':
        return 3;
      case 'JACK':
        return 4;
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
      case 'HEARTS':
        return '♥';
      case 'SPADES':
        return '♠';
      case 'DIAMONDS':
        return '♦︎';
      case 'CLUBS':
        return '♣︎';
    }
  }
}
