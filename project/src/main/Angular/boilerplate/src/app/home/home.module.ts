import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { StartseiteRoutingModule } from './home-routing.module';
import { SharedModule } from '../shared/shared.module';
import { MainModule } from '../main/main.module';
import { MainComponent } from '../main/main/main.component';


@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    StartseiteRoutingModule,
    SharedModule,
    MainComponent
  ]
})
export class StartseiteModule { }
