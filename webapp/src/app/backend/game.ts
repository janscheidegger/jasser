import {Player} from './player';
import {Card} from "./card";
import {Team} from "../team";

export interface Turn {
  cardsOnTable: {card: Card, player: string}[];
}

export interface Game {
  gameId: string;
  players: Player[];
  turns: Turn[];
  type: string;
  moveAllowed: string[];
  step: string;
  teams: Team[];
}
