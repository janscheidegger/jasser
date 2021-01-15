import {State} from './jass.reducer'
import {createFeatureSelector, createSelector} from "@ngrx/store";

export const getJassState = createFeatureSelector<State>('jass');

export const selectHand = (state: State) => state.hand;
export const getPlayers = createSelector(
  getJassState,
  (state: State) => state.players
);

export const getHand = createSelector(
  getJassState,
  (state: State) => state.hand
);
