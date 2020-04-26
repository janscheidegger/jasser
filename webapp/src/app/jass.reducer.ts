import { createReducer, on, Action } from '@ngrx/store';
import { JassState } from './jass.state';
import { playCard } from './jass.actions';

export const initialState: JassState = {
  hand: [
    { rank: 'ACE', suit: 'HEART' },
    { rank: 'KING', suit: 'SPADES' },
  ],
  players: [
    {
      hand: [],
      name: 'jan'
    }
  ],
  table: []
};

const reducer = createReducer(
  initialState,
  on(playCard, (state, { card }) => ({...state, hand: state.hand.filter(c => c !== card), table: [...state.table, card]}))
  );

export function jassReducer(state: JassState | undefined, action: Action) {
  return reducer(state, action);
}
