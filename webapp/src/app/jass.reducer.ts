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
  on(playCard, (state, { card }) => ({
    ...state,
    hand: state.hand.filter((c) => c !== card)
  })),
  on(cardPlayed, (state, {player, card}) => ({
    ...state,
    table: [...state.table, card]
  })),
  on(receiveCard, (state, {card}) => (
     {
    ...state,
    hand: [...state.hand, card]
  })),
  on(errorReceived, (state, {errorMessage}) => (
    {
      ...state,
      error: [...state.errors, errorMessage]
    }
  ))
);

export function jassReducer(state: JassState | undefined, action: Action) {
  return reducer(state, action);
}
