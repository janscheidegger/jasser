import {Action, createReducer, on} from '@ngrx/store';
import {
  cardPlayed,
  cardsReceived,
  errorReceived,
  gameLoaded,
  initialLoad,
  playerJoined,
  playerLeft
} from './jass.actions';
import {Card} from "./backend/card";

export interface State {
  name: string;
  players: string[];
  hand: Card[];
  table: Card[];
  errors: string[];
}

export const initialState: State = {
  name: '',
  hand: [],
  players: [],
  table: [],
  errors: []
};

const reducer = createReducer(
  initialState,
  on(initialLoad, (state, {gameId, name}) => ({
    ...state,
    name
  })),
  on(playerJoined, (state, {player}) => ({
    ...state,
    players: [...state.players, player]
  })),
  on(playerLeft, (state, {player}) => ({
    ...state,
    players: state.players.filter(p => p !== player)
  })),
  on(cardPlayed, (state, {player, card}) => ({
    ...state,
    table: [...state.table, card],
    hand: state.hand.filter((c) => c.rank !== card.rank || c.suit !== card.suit)
  })),
  on(cardsReceived, (state, {cards}) => (
    {
      ...state,
      hand: [...state.hand, ...cards]
    })),
  on(errorReceived, (state, {errorMessage}) => (
    {
      ...state,
      errors: [...state.errors, errorMessage]
    }
  )),
  on(gameLoaded, (state, {game}) => ({
    ...state,
    players: game.players.map(p => p.name),
    hand: game.players.find(n => n.name === state.name).hand,
    table: game.turns[game.turns.length-1].cardsOnTable.map(c => c.card)
  }))
);

export function jassReducer(state: State | undefined, action: Action) {
  return reducer(state, action);
}
