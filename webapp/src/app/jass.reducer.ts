import {Action, createReducer, on} from '@ngrx/store';
import {
  cardPlayed,
  cardsReceived,
  errorReceived,
  gameLoaded,
  initialLoad,
  playerJoined,
  playerLeft, trumpChosen, trumpSelectionReceived
} from './jass.actions';
import {Card} from "./backend/card";

export interface State {
  name: string;
  players: string[];
  hand: Card[];
  table: Card[];
  errors: string[];
  step: string;
  moveAllowed: string[],
  trump: 'HEARTS' | 'SPADES' | 'DIAMONDS' | 'CLUBS'
}

export const initialState: State = {
  name: '',
  hand: [],
  players: [],
  table: [],
  errors: [],
  step: '',
  moveAllowed: [],
  trump: undefined
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
    table: game.turns.length > 0 ? game.turns[game.turns.length-1].cardsOnTable.map(c => c.card) : [],
    moveAllowed: game.moveAllowed,
    step: game.step
  })),
  on(trumpSelectionReceived, (state, {suit}) => ({
    ...state,
    trump: suit
  }))
);

export function jassReducer(state: State | undefined, action: Action) {
  return reducer(state, action);
}
