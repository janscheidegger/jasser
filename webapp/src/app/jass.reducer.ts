import {Action, createReducer, on} from '@ngrx/store';
import {
  cardPlayed,
  cardsReceived,
  errorReceived,
  gameLoaded,
  initialLoad,
  playerJoined,
  playerLeft, roundOver, trumpSelectionReceived, turnWon
} from './jass.actions';
import {Card} from "./backend/card";
import {Team} from "./team";

export interface State {
  name: string;
  players: string[];
  hand: Card[];
  table: Card[];
  errors: string[];
  step: string;
  moveAllowed: string[],
  teams: Team[];
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
  teams: [],
  trump: undefined
};

const reducer = createReducer(
  initialState,
  on(initialLoad, (state, {gameId, name}) => ({
    ...state,
    name
  })),
  on(playerJoined, (state, {nextStep,player}) => ({
    ...state,
    step: nextStep,
    players: [...state.players, player]
  })),
  on(playerLeft, (state, {nextStep, player}) => ({
    ...state,
    step: nextStep,
    players: state.players.filter(p => p !== player)
  })),
  on(cardPlayed, (state, {nextStep,player, card}) => ({
    ...state,
    step: nextStep,
    table: [...state.table, card],
    hand: state.hand.filter((c) => c.rank !== card.rank || c.suit !== card.suit)
  })),
  on(cardsReceived, (state, {nextStep, cards}) => ({
      ...state,
    step: nextStep,
    hand: [...state.hand, ...cards]
    })),
  on(errorReceived, (state, {nextStep, errorMessage}) => (
    {
      ...state,
      step: nextStep,
      errors: [...state.errors, errorMessage]
    }
  )),
  on(gameLoaded, (state, {game}) => ({
    ...state,
    players: game.players.map(p => p.name),
    hand: game.players.find(n => n.name === state.name).hand,
    table: game.turns.length > 0 ? game.turns[game.turns.length-1].cardsOnTable.map(c => c.card) : [],
    moveAllowed: game.moveAllowed,
    step: game.step,
    teams: game.teams
  })),
  on(trumpSelectionReceived, (state, {nextStep, suit}) => ({
    ...state,
    step: nextStep,
    trump: suit
  })),
  on(turnWon, (state, {nextStep, player}) => ({
    ...state,
    step: nextStep,
    table: []
  })),
  on(roundOver, (state, {nextStep, teams}) => ({
    ...state,
    step: nextStep,
    teams: teams
  }))
);

export function jassReducer(state: State | undefined, action: Action) {
  return reducer(state, action);
}
