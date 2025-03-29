import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReadingRoutingModule } from './reading-routing.module';
import { SharedModule } from '../shared/shared.module';
import { MainComponent } from '../main/main/main.component';


@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    ReadingRoutingModule,
    SharedModule,
    MainComponent
  ]
})
export class ReadingModule { }
