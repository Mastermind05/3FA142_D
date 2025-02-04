import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import axios from 'axios';


export interface PeriodicElement {
  position: number;
  dateOfReading: string;
  kindOfMeter: string;
  meterCount: string;
  meterId: string;
  substitute: string;
}

export interface Customer {
  id: number;
  firstName: string;
  lastName: string;
  birthdate: string;
  gender: string;
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
  customer: Customer = {
    id: 0,
    firstName: '',
    lastName: '',
    birthdate: '',
    gender: ''
  };
  addCustomer() {
    const url = 'http://localhost:8080/test/ressources/customers'; // Ersetzen Sie dies durch Ihre API-URL
    axios.post<Customer>(url, this.customer)
      .then(response => {
        console.log('Customer added successfully', response.data);
      })
      .catch(error => {
        console.error('Error adding customer', error);
      });
  }
}
