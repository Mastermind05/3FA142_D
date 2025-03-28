import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from '../shared/shared.module';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-createdialog',
  standalone: true,
  imports: [SharedModule,CommonModule],
  templateUrl: './createdialog.component.html',
  styleUrls: ['./createdialog.component.scss']
})
export class CreatedialogComponent {
  customerdata = {firstName: '', lastName: '', birthDate: '', gender: ''};
  errorMessage: string = '';

  constructor(private dialogRef: MatDialogRef<CreatedialogComponent>) {}

  formatDate(date: any): string {
    if (!date) return ''; // Falls kein Datum vorhanden ist
  
    // Falls das Datum als String kommt, in ein Date-Objekt umwandeln
    const newDate = typeof date === 'string' ? new Date(date) : date;
  
    const day = String(newDate.getDate()).padStart(2, '0');
    const month = String(newDate.getMonth() + 1).padStart(2, '0'); // Monate starten bei 0
    const year = newDate.getFullYear();
  
    return `${year}-${month}-${day}`;
  }

  createuser(){
    this.customerdata.birthDate = this.formatDate(this.customerdata.birthDate);
    console.log(this.customerdata);
    
  if (!this.customerdata.firstName || this.customerdata.firstName.trim() === '') {
    this.errorMessage = 'Bitte geben Sie einen gültigen Namen ein!';
    return; // ⛔ Dialog bleibt offen!
  }
  if (!this.customerdata.lastName || this.customerdata.lastName.trim() === '') {
    this.errorMessage = 'Bitte geben Sie einen gültigen Nachnamen ein!';
    return; // ⛔ Dialog bleibt offen!
  }
  if (!this.customerdata.birthDate || this.customerdata.birthDate.trim() === '') {
    this.errorMessage = 'Bitte geben Sie einen gültiges Geburtsdatum ein!';
    return; // ⛔ Dialog bleibt offen!
  }
  if (!this.customerdata.gender || this.customerdata.gender.trim() === '') {
    this.errorMessage = 'Bitte geben Sie einen gültiges Geschlecht ein!';
    return; // ⛔ Dialog bleibt offen!
  }

  // Wenn alles passt, Dialog schließen & Daten zurückgeben
  this.dialogRef.close(this.customerdata);
  }
}