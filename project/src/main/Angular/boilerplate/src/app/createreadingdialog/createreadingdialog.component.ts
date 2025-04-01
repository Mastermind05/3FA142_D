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

  onSave(): void {
    if (this.readingForm.valid) {
      const updatedReading: Reading = {
        ...this.data,
        ...this.readingForm.value,
        customer: this.customers.find(c => c.id === this.readingForm.value.customer) || this.data.customer
      };
      this.dialogRef.close(updatedReading);
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
