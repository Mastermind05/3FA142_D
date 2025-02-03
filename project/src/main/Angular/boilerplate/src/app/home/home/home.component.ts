import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

export interface PeriodicElement {
  position: number;
  dateOfReading: string;
  kindOfMeter: string;
  meterCount: string;
  meterId: string;
  substitute: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  {position: 1, dateOfReading: 'test', kindOfMeter: 'test', meterCount: 'test', meterId: 'test', substitute: 'test'},
];

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: false,
  styleUrl: './home.component.scss',
})



export class HomeComponent {
  displayedColumns: string[] = ['position', 'dateofreading', 'kindofmeter', 'metercount', 'meterid', 'substitute'];
  dataSource = ELEMENT_DATA;
}
