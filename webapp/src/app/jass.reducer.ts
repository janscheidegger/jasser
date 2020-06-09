import { createReducer, on, Action } from '@ngrx/store';
import { JassState } from './jass.state';
import { playCard, initialLoad, receiveCard, errorReceived, cardPlayed } from './jass.actions';

export const initialState: JassState = {
  hand: [],
  players: [
    {
      hand: [],
      name: 'jan',
    },
  ],
  table: [],
  errors: []
};

const reducer = createReducer(
  initialState,
  on(initialLoad, (state, { cards }) => ({ ...state, hand: cards, table: [] })),
  on(cardPlayed, (state, {player, card}) => ({
    ...state,
    table: [...state.table, card],
    hand: state.hand.filter((c) => c.rank !== card.rank || c.suit !== card.suit)
  })),
  on(receiveCard, (state, {card}) => (
     {
    ...state,
    hand: [...state.hand, card]
  })),
  on(errorReceived, (state, {errorMessage}) => (
    {
      ...state,
      errors: [...state.errors, errorMessage]
    }
  ))
);

export function jassReducer(state: JassState | undefined, action: Action) {
  return reducer(state, action);
}
