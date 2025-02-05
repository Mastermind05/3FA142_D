import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { StartseiteRoutingModule } from './home-routing.module';
import { SharedModule } from '../shared/shared.module';
import { CustomertableComponent } from '../customertable/customertable.component';

@NgModule({
  declarations: [
    HomeComponent
  ],
  imports: [
    CommonModule,
    StartseiteRoutingModule,
    SharedModule,
    CustomertableComponent
  ]
})
export class StartseiteModule { }
