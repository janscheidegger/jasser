import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatInputModule } from '@angular/material/input';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { StoreModule } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { StoreDevtoolsModule } from '@ngrx/store-devtools';
import { HttpClientModule } from '@angular/common/http';
import { GameComponent } from './game/game.component';
import { HandComponent } from './hand/hand.component';
import { TableComponent } from './table/table.component';
import { jassReducer } from './jass.reducer';
import { CardComponent } from './card/card.component';
import { JassEffects } from './jass.effects';

@NgModule({
  declarations: [
    AppComponent,
    GameComponent,
    HandComponent,
    TableComponent,
    CardComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatInputModule,
    MatCardModule,
    MatButtonModule,
    StoreModule.forRoot({ jass: jassReducer }),
    EffectsModule.forRoot([JassEffects]),
    StoreDevtoolsModule.instrument({
      maxAge: 10,
    }),
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
