import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HomeComponent } from './home/home/home.component';
import { LoginComponent } from './login/login/login.component';
import { authGuard } from '../guard/auth.guard';
import { ReadingComponent } from './reading/reading/reading.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: '', component: HomeComponent, canActivate: [authGuard] },  // Home nur mit AuthGuard zugänglich
  { path: 'reading', component: ReadingComponent, canActivate: [authGuard] }, // Reading nur mit AuthGuard zugänglich
  { path: '**', redirectTo: '/login' },  // Unbekannte Routen führen zum Login  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
