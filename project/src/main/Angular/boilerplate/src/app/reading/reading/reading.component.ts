import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import axios from 'axios';

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

  constructor(private route: ActivatedRoute) { }

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
}
