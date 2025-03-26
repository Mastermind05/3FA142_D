import { Component } from '@angular/core';
import { DialogService } from '../../services/dialog.service';
import axios from 'axios';

export interface Person {
  id: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: string;
}

const baseurl = 'http://localhost:8080/test/ressources/customers';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: false,
  styleUrl: './home.component.scss',
})
export class HomeComponent {
  constructor(private dialogService: DialogService) {}

  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'birthDate', 'gender', 'actions'];
  dataSource: Person[] = []; // Startet als leeres Array

  ngOnInit() {
    this.getAllCustomers(); // ⏳ Lädt die Kunden beim Seitenstart
  }

  async getAllCustomers() {
    try {
      const response = await axios.get<Person[]>(baseurl);
      this.dataSource = response.data; // 🔄 Tabelle mit Server-Daten aktualisieren
      console.log('📥 Kunden erfolgreich geladen:', this.dataSource);
    } catch (error) {
      console.error('❌ Fehler beim Abrufen der Kunden:', error);
    }
  }
  
  //(click)="deleteUser(element.id)"
  async deleteUser(id: string) {
    const url = `http://localhost:8080/test/ressources/customers/${id}`;

    try {
      const response = await axios.delete(url);
      console.log('Erfolgreich gelöscht:', response.data);
      
      // Optional: Entferne den gelöschten User aus der Tabelle
      this.dataSource = this.dataSource.filter(user => user.id !== id);
    } catch (error) {
      console.error('Fehler beim Löschen:', error);
    }
  }

  async openDialog() {
    try {
      const result = await this.dialogService.openDialog();
      if (result) {
        console.log('Empfangene Daten aus Dialog:', result);

        try {
          const response = await axios.post(baseurl, result, {
            headers: { 'Content-Type': 'application/json' },
          });

          console.log('✅ Erfolgreich gespeichert:', response.data);
          this.getAllCustomers(); // ⏳ Nach dem Speichern die Liste neu laden
        } catch (error) {
          console.error('❌ Fehler beim Speichern:', error);
        }
      }
    } catch (error) {
      console.error('Fehler beim Öffnen des Dialogs:', error);
    }
  }
}
