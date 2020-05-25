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

  numValue(card: Card): {name: string, num: number} {
    switch (card.rank) {
      case 'ACE':
        return {name: 'A', num: 1};
      case 'KING':
        return {name: 'K', num: 2};
      case 'QUEEN':
        return {name: 'Q', num: 1};
      case 'JACK':
        return {name: 'J', num: 1};
      case 'SIX':
        return {name: '6', num: 6};
      case 'SEVEN':
        return {name: '7', num: 7};
      case 'EIGHT':
        return {name: '8', num: 8};
      case 'NINE':
        return {name: '9', num: 9};
      case 'TEN':
        return {name: '10', num: 10};
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
