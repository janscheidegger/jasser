import { createAction, props } from '@ngrx/store';

export const playCard = createAction('[Hand Component] Play Card', props<{card: any}>());
