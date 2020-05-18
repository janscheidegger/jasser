import { Player } from './backend/player';
import { Card } from './backend/card';

export interface JassState {
  players: Player[];
  hand: Card[];
  table: Card[];
  errors: string[];
}
