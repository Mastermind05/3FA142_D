import { Component } from '@angular/core';
import { DialogService } from '../../services/dialog.service';
import axios from 'axios';
import { MatDialog } from '@angular/material/dialog';
import { UpdatedialogComponent } from '../../updatedialog/updatedialog.component';
import { Router } from '@angular/router';
import { SettingdialogComponent } from '../../settingdialog/settingdialog.component';
import JSZip from 'jszip';
import { saveAs } from 'file-saver';
import * as Papa from 'papaparse';
import * as xml2js from 'xml2js';


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
  constructor(private dialogService: DialogService, public dialog: MatDialog, private router: Router) {}
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
  
  editingUser: any = null;

  // Bearbeiten-Methoden
  editUser(user: any) {
    this.editingUser = { ...user }; // Kopiere die Daten, um sie zu bearbeiten
  }

  cancelEdit() {
    this.editingUser = null; // Setze die Bearbeitung zurück
  }

  openEditDialog(user: any): void {
    const dialogRef = this.dialog.open(UpdatedialogComponent, {
      width: '400px',
      data: { ...user } // Kopiere die Benutzerdaten in den Dialog
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.updateUser(result); // Wenn Daten zurückgegeben wurden, die Benutzerinformationen aktualisieren
      }
    });
  }
  openSettingDialog(): void {
    const dialogRef = this.dialog.open(SettingdialogComponent, {
      width: '40%',
      height: '40%',
    });
  }
  // Update-Methode
  async updateUser(user: any) {
    const url = `http://localhost:8080/test/ressources/customers/`;

    try {
      console.log(user)
      const response = await axios.put(url, user);
      console.log('Erfolgreich aktualisiert:', response.data);

      // Nach erfolgreichem Update die Daten in der Tabelle aktualisieren
      const index = this.dataSource.findIndex(u => u.id === user.id);
      if (index !== -1) {
        this.dataSource[index] = { ...user };
      }
    } catch (error) {
      console.error('Fehler beim Aktualisieren:', error);
    }
  }
  
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
  goToReading(id: string) {
    console.log('Session Storage:', sessionStorage.getItem('isAuthenticated'));
    this.router.navigate(['/reading', id]);
}


exportAsZip(): void {
    if (this.dataSource.length === 0) {
      console.warn('⚠️ Keine Daten zum Exportieren!');
      return;
    }
  
    const zip = new JSZip();
  
    // JSON-Datei erstellen
    const jsonContent = JSON.stringify(this.dataSource, null, 2);
    zip.file('customers.json', jsonContent);
  
    // CSV-Datei erstellen
    const csvHeader = 'id,fistName,lastName,birthDate,gender\n';
    const csvContent = this.dataSource
      .map(r => `${r.id},${r.firstName},${r.lastName},${r.birthDate},${r.gender}`)
      .join('\n');
    zip.file('customers.csv', csvHeader + csvContent);
    // XML-Datei erstellen
    let xmlContent = '<?xml version="1.0" encoding="UTF-8"?>\n<customers>\n';
    this.dataSource.forEach(r => {
      xmlContent += `  <customer>\n`;
      xmlContent += `    <id>${r.id}</id>\n`;
      xmlContent += `    <firstName>${r.firstName}</firstName>\n`;
      xmlContent += `    <lastName>${r.lastName}</lastName>\n`;
      xmlContent += `    <birthDate>${r.birthDate}</birthDate>\n`;
      xmlContent += `    <gender>${r.gender}</gender>\n`;
      xmlContent += `  </customer>\n`;
    });
    xmlContent += '</customers>';
    zip.file('customers.xml', xmlContent);
  
    // ZIP-Datei generieren und speichern
    zip.generateAsync({ type: 'blob' }).then(content => {
      saveAs(content, 'customers.zip');
    });
}

async importCustomers(event: any) {
  const file = event.target.files[0];
  if (!file) return;

  const reader = new FileReader();
  reader.onload = async (e: any) => {
    const content = e.target.result;
    let customers: any[] = [];

    if (file.name.endsWith('.json')) {
      const jsonData = JSON.parse(content);
      customers = Array.isArray(jsonData) ? jsonData : [jsonData]; // Sicherstellen, dass es ein Array ist
    } 
    else if (file.name.endsWith('.csv')) {
      const parsedCSV = Papa.parse(content, { header: true, skipEmptyLines: true }).data;
      customers = Array.isArray(parsedCSV) ? parsedCSV : [parsedCSV]; // Sicherstellen, dass es ein Array ist
    } 
    else if (file.name.endsWith('.xml')) {
      customers = await new Promise<any[]>((resolve, reject) => {
        xml2js.parseString(content, { explicitArray: false }, (err, result) => {
          if (err) return reject(err);
          let parsedCustomers = result?.customers?.customer || [];
          resolve(Array.isArray(parsedCustomers) ? parsedCustomers : [parsedCustomers]);
        });
      });
    }

    // Kunden verarbeiten (jeder Eintrag wird einzeln gesendet)
    for (const customer of customers) {
      await this.createCustomer(customer);
    }
  };
  reader.readAsText(file);
}


async createCustomer(customer: any) {
  try {
    const response = await axios.post('http://localhost:8080/test/ressources/customers', customer);
    console.log('📤 Customer erfolgreich erstellt:', response);
  } catch (error) {
    console.error('❌ Fehler beim Erstellen des Customers:', error);
  }
}
openFileInput(): void {
  const input = document.createElement('input');
  input.type = 'file';
  input.accept = '.json,.csv,.xml';
  input.addEventListener('change', (event) => this.importCustomers(event));
  input.click();
}
}
