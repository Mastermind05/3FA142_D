import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import axios from 'axios';
import { Customer, Reading } from '../reading/reading/reading.component';
import { SharedModule } from '../shared/shared.module';

const customersUrl = 'http://localhost:8080/test/ressources/customers'; // Kunden-API
const updateReadingUrl = 'http://localhost:8080/test/ressources/readings'; // Update-API

@Component({
  selector: 'app-update-reading-dialog',
  templateUrl: './updatereadingdialog.component.html',
  styleUrls: ['./updatereadingdialog.component.scss'],
  standalone: false,
})
export class UpdateReadingDialogComponent implements OnInit {
  updateForm: FormGroup;
  customers: Customer[] = [];

  constructor(
    public dialogRef: MatDialogRef<UpdateReadingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Reading,
    private fb: FormBuilder
  ) {
    this.updateForm = this.fb.group({
      id: [{ value: data.id, disabled: true }], // ID ist nicht editierbar
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
      const response = await axios.get<Customer[]>(customersUrl);
      this.customers = response.data;
      console.log('📥 Kunden geladen:', this.customers);
    } catch (error) {
      console.error('❌ Fehler beim Laden der Kunden:', error);
    }
  }

  async onSave(): Promise<void> {
    if (this.updateForm.valid) {
      const updatedReading: Reading = {
        ...this.data,
        ...this.updateForm.value,
        customer: this.customers.find(c => c.id === this.updateForm.value.customer) || this.data.customer
      };

      try {
        await axios.put(`${updateReadingUrl}/${updatedReading.id}`, updatedReading);
        console.log('✅ Reading erfolgreich aktualisiert:', updatedReading);
        this.dialogRef.close(updatedReading);
      } catch (error) {
        console.error('❌ Fehler beim Update:', error);
      }
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
