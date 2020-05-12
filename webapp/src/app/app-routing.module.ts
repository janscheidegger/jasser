import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GameComponent } from './game/game.component';
import { OverviewComponent } from './overview/overview.component';

const routes: Routes = [
  { path: 'games/:gameId', component: GameComponent },
  { path: '', component: OverviewComponent, pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
