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

export const getCardsOnTable = createSelector(
  getJassState,
  (state: State) => state.table
)

export const canSelectTrump = createSelector(
  getJassState,
  (state) => state.moveAllowed.includes(state.name) && state.step === 'CHOOSE_TRUMP'
)

export const currentState = createSelector(
  getJassState,
    state => state.step
)

export const pointsPerTeam = createSelector(
  getJassState,
  state => state.teams.map(t => ({name: t.name, points: t.points}))
)
