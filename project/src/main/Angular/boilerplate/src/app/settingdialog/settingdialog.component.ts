import { Component } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { MatDialogRef } from '@angular/material/dialog';
import axios from 'axios';

@Component({
  selector: 'app-settingdialog',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './settingdialog.component.html',
  styleUrl: './settingdialog.component.scss'
})
export class SettingdialogComponent {
  isYesSelected: boolean = false; // Steuert, ob der Button aktiv ist oder nicht
  constructor(private dialogRef: MatDialogRef<SettingdialogComponent>) {}

  // Wird aufgerufen, wenn der Benutzer eine Auswahl trifft
  onChoiceChange(event: Event) {
    const selectElement = event.target as HTMLSelectElement;
    this.isYesSelected = selectElement.value === 'yes'; // Button wird nur aktiviert, wenn "Ja" ausgewählt wurde
  }

  // Aktion ausführen, wenn der Button geklickt wird
  onAction() {
    this.delteDB();
  }

  // Dialog schließen
  closeDialog() {
    this.dialogRef.close();
  }

  async delteDB() {
    const url = `http://localhost:8080/test/ressources/setupDB`;

    try {
      console.log('Datebank wird gelöscht');
      const response = await axios.delete(url);
      console.log('Datenbank gelöscht:', response);
    } catch (error) {
      console.error('Fehler beim Aktualisieren:', error);
    }
  }
}
