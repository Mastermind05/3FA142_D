import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';
import { SettingdialogComponent } from '../../settingdialog/settingdialog.component';
import { MatDialog } from '@angular/material/dialog';
import JSZip from 'jszip';
import { saveAs } from 'file-saver';
import { CreatereadingdialogComponent } from '../../createreadingdialog/createreadingdialog.component';
import { UpdateReadingDialogComponent } from '../../updatereadingdialog/updatereadingdialog.component';
import * as Papa from 'papaparse';
import * as xml2js from 'xml2js';


export interface Customer {
  id: string;
  firstName: string;
  lastName: string;
  birthDate: string;
  gender: string;
}

export interface Reading {
  id: string;
  kindOfMeter: string;
  dateOfReading: [];
  comment: string;
  meterCount: number;
  meterId: string;
  substitute: boolean;
  customer: Customer;  
}

const baseurl = 'http://localhost:8080/test/ressources/readings';

@Component({
  selector: 'app-reading',
  templateUrl: './reading.component.html',
  standalone: false,
  styleUrls: ['./reading.component.scss']
})
export class ReadingComponent {

  constructor(private route: ActivatedRoute, public dialog: MatDialog) { }

  dataSource: Reading[] = [];

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
        const customerId = params.get('id');  // ID aus der URL holen
        console.log('Received Customer ID:', customerId);
        if (customerId) {  // Sicherstellen, dass customerId nicht null ist
          console.log('✅ Received Customer ID:', customerId);
          this.getReading(customerId);  // getReading mit customerId aufrufen
        } else {
          console.error('❌ Fehler: Keine Customer ID in der URL gefunden!');
        }
    });
  }

  displayedColumns: string[] = [
    'id',
    'comment',
    'dateOfReading',
    'kindOfMeter',
    'meterCount',
    'meterId',
    'substitute',
    'customer_id',  // Spalte für customer_id
    'actions'
  ];

  // Methode, um mit dem customer-Objekt zu arbeiten
  async getReading(customerId: string) {
    try {
      const response = await axios.get<Reading[]>(`${baseurl}?customer=${customerId}`);
      
      // Speichere die response.data in der dataSource
      this.dataSource = response.data;  // Da API das customer-Objekt enthält, können wir es direkt verwenden

      console.log('📥 Ablesedaten erfolgreich geladen:', this.dataSource);
    } catch (error) {
      console.error('❌ Fehler beim Abrufen der Ablesedaten:', error);
    }
  }
  openSettingDialog(): void {
      const dialogRef = this.dialog.open(SettingdialogComponent, {
        width: '40%',
        height: '40%',
      });
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(CreatereadingdialogComponent, {
      width: '500px',
      data: {
        id: '',
        kindOfMeter: '',
        dateOfReading: [],
        comment: '',
        meterCount: 0,
        meterId: '',
        substitute: false,
        customer: {
          id: '',
          firstName: '',
          lastName: '',
          birthDate: '',
          gender: ''
        } as Customer
      } as Reading
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        console.log('📄 Neuer Reading-Eintrag:', result);
        // Hier kannst du die Logik zum Speichern oder Aktualisieren hinzufügen
      }
    });
  }

  openUpdateDialog(reading: Reading): void {
    const dialogRef = this.dialog.open(UpdateReadingDialogComponent, {
      width: '500px',
      data: { ...reading } // Übergibt das aktuelle Reading zum Bearbeiten
    });
  
    dialogRef.afterClosed().subscribe(updatedReading => {
      if (updatedReading) {
        console.log('✅ Reading wurde aktualisiert:', updatedReading);
        // Hier kannst du die Tabelle oder das UI aktualisieren
      }
    });
  }

  async deleteReading(id: string) {
    const url = `http://localhost:8080/test/ressources/readings/${id}`;

    try {
      const response = await axios.delete(url);
      console.log('Erfolgreich gelöscht:', response.data);
      
      // Optional: Entferne den gelöschten User aus der Tabelle
      this.dataSource = this.dataSource.filter(reading => reading.id !== id);
    } catch (error) {
      console.error('Fehler beim Löschen:', error);
    }
  }

  exportAsZip(): void {
    if (this.dataSource.length === 0) {
      console.warn('⚠️ Keine Daten zum Exportieren!');
      return;
    }
  
    const zip = new JSZip();
  
    // JSON-Datei erstellen
    const jsonContent = JSON.stringify(this.dataSource, null, 2);
    zip.file('readings.json', jsonContent);
  
    // CSV-Datei erstellen
    const csvHeader = 'id,kindOfMeter,dateOfReading,comment,meterCount,meterId,substitute,customer_id\n';
    const csvContent = this.dataSource
      .map(r => `${r.id},${r.kindOfMeter},${r.dateOfReading},${r.comment},${r.meterCount},${r.meterId},${r.substitute},${r.customer.id}`)
      .join('\n');
    zip.file('readings.csv', csvHeader + csvContent);
  
    // XML-Datei erstellen
    let xmlContent = '<?xml version="1.0" encoding="UTF-8"?>\n<readings>\n';
    this.dataSource.forEach(r => {
      xmlContent += `  <reading>\n`;
      xmlContent += `    <id>${r.id}</id>\n`;
      xmlContent += `    <kindOfMeter>${r.kindOfMeter}</kindOfMeter>\n`;
      xmlContent += `    <dateOfReading>${r.dateOfReading}</dateOfReading>\n`;
      xmlContent += `    <comment>${r.comment}</comment>\n`;
      xmlContent += `    <meterCount>${r.meterCount}</meterCount>\n`;
      xmlContent += `    <meterId>${r.meterId}</meterId>\n`;
      xmlContent += `    <substitute>${r.substitute}</substitute>\n`;
      xmlContent += `    <customer_id>${r.customer.id}</customer_id>\n`;
      xmlContent += `  </reading>\n`;
    });
    xmlContent += '</readings>';
    zip.file('readings.xml', xmlContent);
  
    // ZIP-Datei generieren und speichern
    zip.generateAsync({ type: 'blob' }).then(content => {
      saveAs(content, 'readings.zip');
    });
}

async importReadings(event: any) {
  const file = event.target.files[0];
  if (!file) return;
  
  const reader = new FileReader();
  reader.onload = async (e: any) => {
    const content = e.target.result;
    let readings: any[] = [];
    
    if (file.name.endsWith('.json')) {
      readings = JSON.parse(content);
    } else if (file.name.endsWith('.csv')) {
      readings = Papa.parse(content, { header: true, skipEmptyLines: true }).data;
    } else if (file.name.endsWith('.xml')) {
      xml2js.parseString(content, { explicitArray: false }, (err, result) => {
        if (!err) readings = result.readings.reading;
      });
    }
    
    for (const reading of readings) {
      await this.createReading(reading);
    }
  };
  reader.readAsText(file);
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

}
