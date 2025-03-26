import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ReadingComponent } from './reading/reading.component';

const routes: Routes = [
  {path: 'reading', component: ReadingComponent}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ReadingRoutingModule { }
