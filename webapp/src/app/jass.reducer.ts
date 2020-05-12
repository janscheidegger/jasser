import { createReducer, on, Action } from '@ngrx/store';
import { JassState } from './jass.state';
import { playCard, initialLoad, receiveCard } from './jass.actions';

export const initialState: JassState = {
  hand: [
    { rank: 'NINE', suit: 'HEARTS' },
    { rank: 'SIX', suit: 'SPADES' },
  ],
  players: [
    {
      hand: [],
      name: 'jan',
    },
  ],
  table: [],
};

const reducer = createReducer(
  initialState,
  on(initialLoad, (state, { cards }) => ({ ...state, hand: cards, table: [] })),
  on(playCard, (state, { card }) => ({
    ...state,
    hand: state.hand.filter((c) => c !== card),
    table: [...state.table, card],
  })),
  on(receiveCard, (state, {card}) => (
     {
    ...state,
    hand: [...state.hand, card]
  }))
);

export function jassReducer(state: JassState | undefined, action: Action) {
  return reducer(state, action);
}
