import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ReadingRoutingModule } from './reading-routing.module';
import { ReadingComponent } from './reading/reading.component';


@NgModule({
  declarations: [
    ReadingComponent
  ],
  imports: [
    CommonModule,
    ReadingRoutingModule
  ]
})
export class ReadingModule { }
