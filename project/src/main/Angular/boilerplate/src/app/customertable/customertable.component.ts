import { Component } from '@angular/core';
import { SharedModule } from '../shared/shared.module';

export interface PeriodicElement {
  id: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
];


@Component({
  selector: 'app-customertable',
  imports: [SharedModule],
  templateUrl: './customertable.component.html',
  styleUrl: './customertable.component.scss'
})
export class CustomertableComponent {
  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'birthDate', 'gender'];
  dataSource = ELEMENT_DATA;
}
