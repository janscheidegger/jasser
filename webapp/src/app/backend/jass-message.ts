import {Card} from "./card";
import {Team} from "../team";

export interface JassMessage {
  nextStep: string; // TODO: ENUM!
  event: string; // TODO: ENUM!
  username: string;
  hand: Card[];
  cards: Card[];
  teams: Team[];
  message: string;
  chosenTrump: 'SPADES' | 'HEARTS' | 'CLUBS' | 'DIAMONDS';

}
