import {Card} from "./card";

export interface JassMessage {
  event: string;
  username: string;
  hand: Card[];
  cards: Card[];
  teams: any[];
  message: string;
  chosenTrump: 'SPADES' | 'HEARTS' | 'CLUBS' | 'DIAMONDS';

}
