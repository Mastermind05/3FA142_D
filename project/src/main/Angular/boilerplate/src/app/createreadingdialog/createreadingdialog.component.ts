import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Customer, Reading } from '../reading/reading/reading.component';
import axios from 'axios';
import { SharedModule } from '../shared/shared.module';

const baseurl = 'http://localhost:8080/test/ressources/customers';

@Component({
  selector: 'app-createreadingdialog',
  standalone: false,
  
  templateUrl: './createreadingdialog.component.html',
  styleUrl: './createreadingdialog.component.scss'
})
export class CreatereadingdialogComponent {
  readingForm: FormGroup;
  customers: Customer[] = [];

  constructor(
    public dialogRef: MatDialogRef<CreatereadingdialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Reading,
    private fb: FormBuilder
  ) {
    this.readingForm = this.fb.group({
      id: [{ value: data.id, disabled: true }], // ID ist nur lesbar
      comment: [data.comment, Validators.required],
      dateOfReading: [data.dateOfReading, Validators.required],
      kindOfMeter: [data.kindOfMeter, Validators.required],
      meterCount: [data.meterCount, [Validators.required, Validators.min(0)]],
      meterId: [data.meterId, Validators.required],
      customer: [data.customer?.id, Validators.required], // Speichert nur die ID
      substitute: [data.substitute]
    });
  }

  ngOnInit(): void {
    this.getAllCustomers();
  }

  async getAllCustomers() {
    try {
      const response = await axios.get<Customer[]>(baseurl);
      this.customers = response.data;
      console.log('📥 Kunden erfolgreich geladen:', this.customers);
    } catch (error) {
      console.error('❌ Fehler beim Abrufen der Kunden:', error);
    }
  }

  async onSave(): Promise<void> {
    if (this.readingForm.valid) {
      const newReading: Reading = {
        ...this.data,
        ...this.readingForm.value,
        dateOfReading: this.formatDate(this.readingForm.value.dateOfReading),
        customer: this.customers.find(c => c.id === this.readingForm.value.customer) || this.data.customer
      };
  
      try {
        const response = await axios.post('http://localhost:8080/test/ressources/readings', newReading);
        console.log('📤 Reading erfolgreich erstellt:', response);
        this.dialogRef.close(newReading);
      } catch (error) {
        console.error('❌ Fehler beim Erstellen des Readings:', error);
      }
    }
  }
  
  
  async createReading(reading: any) {
    try {
      reading.dateOfReading = reading.dateOfReading.map(this.formatDate);
      const response = await axios.post(baseurl, reading);
      console.log('📤 Reading erfolgreich erstellt:', response);
    } catch (error) {
      console.error('❌ Fehler beim Erstellen des Readings:', error);
    }
  }

  formatDate(date: any): string {
    if (!date) return '';
    const newDate = new Date(date);
    const day = String(newDate.getDate()).padStart(2, '0');
    const month = String(newDate.getMonth() + 1).padStart(2, '0');
    const year = newDate.getFullYear();
    return `${year}-${month}-${day}`;
  }
  

  onCancel(): void {
    this.dialogRef.close();
  }
}
