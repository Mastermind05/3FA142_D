import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { MainComponent } from './main/main/main.component';
import { HomeComponent } from './home/home/home.component';

const routes: Routes = [
  {path:'', component: HomeComponent,},
  {path:'main', component: MainComponent}
  ]
;

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
