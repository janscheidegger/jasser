import { createAction, props } from '@ngrx/store';
import { Card } from './backend/card';
import {Game} from "./backend/game";
import {Team} from "./team";

export const playerJoined = createAction('[Jass Game] Player Joined', props<{player: string}>());
export const playerLeft = createAction('[Jass Game] Player Left', props<{player: string}>());
export const playCard = createAction('[Hand Component] Play Card', props<{player: string, card: Card}>());
export const cardReceived = createAction('[Hand Component] Card Received', props<{card: Card}>());
export const initialLoad = createAction('[Hand Component] Initial load', props<{gameId: string}>());
export const cardPlayed = createAction('[Hand Component] Card played', props<{player: string, card: Card}>());
export const errorReceived = createAction('[Jass Game] Error', props<{errorMessage: string}>());
export const gameLoaded = createAction('[Jass Game] Game Loaded', props<{game: Game}>());
export const teamsChosen = createAction('[Teams] Teams  Chosen', props<{teams: Team[]}>());

