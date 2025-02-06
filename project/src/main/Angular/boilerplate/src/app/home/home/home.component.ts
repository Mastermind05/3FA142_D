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
  id: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: string;
}

export interface Reading {
  id: string;
  dateOfReading: string;
  kindOfMeter: string;
  meterCount: string;
  meterId: string;
  substitute: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  ];

const baseurl = 'http://localhost:8080/test/ressources/';

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
    id: '',
    firstName: '',
    lastName: '',
    birthDate: '',
    gender: ''
  };
  reading: Reading = {
    id: '',
    dateOfReading: '',
    kindOfMeter: '',
    meterCount: '',
    meterId: '',
    substitute: ''
  };
  
  addCustomer() {
    const url = `${baseurl}customers`; 
      axios.post<Customer>(url, this.customer)
      .then(response => {
        console.log('Customer added successfully', response.data);
      })
      .catch(error => {
        console.error('Error adding customer', error);
      });
      console.log(this.customer);
  }
  deleteCustomer() {
    const url = `${baseurl}customers/${this.customer.id}`;
    axios.delete<Customer>(url)
      .then(response => {
        console.log('Customer deleted successfully', response.data);
      })
      .catch(error => {
        console.error('Error deleting customer', error);
      });
      console.log(this.customer.id);
  }
  updateCustomer() {
    const url = `${baseurl}customers`; 
    axios.put<Customer>(url, this.customer)
      .then(response => {
        console.log('Customer updated successfully', response.data);
      })
      .catch(error => {
        console.error('Error updating customer', error);
      });
      console.log(this.customer);
  }
  getCustomer() {
    const url = `${baseurl}customers/${this.customer.id}`;
    axios.get<Customer>(url)
      .then(response => {
        console.log('Found customer successfully', response.data);
      })
      .catch(error => {
        console.error('There is no customer with this UUID', error);
      });
      console.log(this.customer);
  }
  addReading() {
    const url = `${baseurl}readings`; 
    axios.post<Reading>(url, this.reading)
      .then(response => {
        console.log('Reading created successfully', response.data);
      })
      .catch(error => {
        console.error('Error creating reading', error);
      });
      console.log(this.customer);
  }
  getReading() {
    const url = `${baseurl}customers/${this.reading.id}`; 
    axios.get<Reading>(url)
      .then(response => {
        console.log('Reading found successfully', response.data);
      })
      .catch(error => {
        console.error('Error searching reading', error);
      });
      console.log(this.customer);
  }
  updateReading() {
    const url = `${baseurl}readings`; 
    axios.put<Reading>(url, this.reading)
      .then(response => {
        console.log('Reading updated successfully', response.data);
      })
      .catch(error => {
        console.error('Error updating reading', error);
      });
      console.log(this.customer);
  }
  deleteReading() {
    const url = `${baseurl}readings/${this.reading.id}`; 
    axios.delete<Customer>(url)
      .then(response => {
        console.log('Reading deleted successfully', response.data);
      })
      .catch(error => {
        console.error('Error deleting reading', error);
      });
      console.log(this.customer);
  }
}
