import {Action, createReducer, on} from '@ngrx/store';
import {cardPlayed, cardReceived, errorReceived, playerJoined} from './jass.actions';
import {Card} from "./backend/card";

export interface State {
  players: string[];
  hand: Card[];
  table: Card[];
  errors: string[];
}

export const initialState: State = {
  hand: [],
  players: [],
  table: [],
  errors: []
};

const reducer = createReducer(
  initialState,
  on(playerJoined, (state, {player}) => ({
    ...state,
    players: [...state.players, player]
  })),
  on(cardPlayed, (state, {player, card}) => ({
    ...state,
    table: [...state.table, card],
    hand: state.hand.filter((c) => c.rank !== card.rank || c.suit !== card.suit)
  })),
  on(cardReceived, (state, {card}) => (
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

export function jassReducer(state: State | undefined, action: Action) {
  return reducer(state, action);
}
