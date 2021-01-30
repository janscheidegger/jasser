import { createAction, props } from '@ngrx/store';
import { Card } from './backend/card';
import {Game} from "./backend/game";
import {Team} from "./team";

export const playerJoined = createAction('[Jass Game] Player Joined', props<{player: string}>());
export const playerLeft = createAction('[Jass Game] Player Left', props<{player: string}>());
export const playCard = createAction('[Hand Component] Play Card', props<{player: string, card: Card}>());
export const handOutCards = createAction('[Jass Game] Hand out Cards');
export const cardsReceived = createAction('[Hand Component] Card Received', props<{cards: Card[]}>());
export const initialLoad = createAction('[Hand Component] Initial load', props<{gameId: string, name: string}>());
export const cardPlayed = createAction('[Hand Component] Card played', props<{player: string, card: Card}>());
export const errorReceived = createAction('[Jass Game] Error', props<{errorMessage: string}>());
export const gameLoaded = createAction('[Jass Game] Game Loaded', props<{game: Game}>());
export const teamsChosen = createAction('[Teams] Teams  Chosen', props<{teams: Team[]}>());
export const chooseTrump = createAction('[Jass Game] Choose Trump');
export const trumpChosen = createAction('[Jass Game] Trump chosen', props<{suit: any}>());
export const trumpSelectionReceived = createAction('[Jass Game] Trump selection received', props<{suit: any}>());
export const schieben = createAction('[Jass Game] Schieben');
