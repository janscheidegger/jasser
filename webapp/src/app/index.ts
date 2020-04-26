import { createSelector } from '@ngrx/store';
import { JassState } from './jass.state';


export const selectHand = (state: JassState) => state.hand;
