import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';
import { SettingdialogComponent } from '../../settingdialog/settingdialog.component';
import { MatDialog } from '@angular/material/dialog';
import JSZip from 'jszip';
import { saveAs } from 'file-saver';

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
  dateOfReading: Date[];
  comment: string;
  meterCount: number;
  meterId: string;
  substitute: boolean;
  customer: Customer;  // customer ist jetzt ein Objekt
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
}
