import { Component } from '@angular/core';
import { DialogService } from '../../services/dialog.service';

export interface Person {
  id: number;
  firstName: string;
  lastName: string;
  birthdate: string;
  gender: string;
}


const baseurl = 'http://localhost:8080/test/ressources/';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  standalone: false,
  styleUrl: './home.component.scss',
})

export class HomeComponent {
  constructor(private dialogService: DialogService) {}
  displayedColumns: string[] = ['id', 'firstName', 'lastName', 'birthdate', 'gender', 'actions'];
  dataSource = [
    { 
      id: 1, 
      firstName: 'Max', 
      lastName: 'Mustermann', 
      birthdate: '1990-05-15', 
      gender: 'male' 
    }
  ];
  openDialog() {
    this.dialogService.openDialog();
  }
}
