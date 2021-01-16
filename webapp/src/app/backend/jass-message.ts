import {Card} from "./card";
import {Team} from "../team";

export interface JassMessage {
  event: string;
  username: string;
  hand: Card[];
  cards: Card[];
  teams: Team[];
  message: string;
  chosenTrump: 'SPADES' | 'HEARTS' | 'CLUBS' | 'DIAMONDS';

}
