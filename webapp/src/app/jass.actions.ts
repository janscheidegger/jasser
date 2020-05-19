import { createAction, props } from '@ngrx/store';
import { Card } from './backend/card';

export const playCard = createAction('[Hand Component] Play Card', props<{player: string, card: Card}>());
export const receiveCard = createAction('[Hand Component] Received Card', props<{card: Card}>());
export const initialLoad = createAction('[Hand Component] Initial load', props<{cards: Card[]}>());
export const cardPlayed = createAction('[Hand Component] Card played', props<{player: string, card: Card}>());
export const errorReceived = createAction('[Jass Game] Error', props<{errorMessage: string}>());

