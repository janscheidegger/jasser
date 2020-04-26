import { Player } from './backend/player';

export interface JassState {
  players: Player[];
  hand: any[];
  table: any[];
}
