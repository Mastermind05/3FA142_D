import { Component } from '@angular/core';
import { Router } from '@angular/router';
import axios from 'axios';  // Axios importieren
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: false,
  
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  errorMessage: string = '';

  constructor(private router: Router) {}

  // login Methode
  login() {
    const loginPayload = {
      username: this.username,
      password: this.password
    };

    axios.post('http://localhost:8080/test/ressources/auth/login', loginPayload)
      .then(response => {
        // Erfolgreiche Antwort
        console.log('Login erfolgreich:', response);
        sessionStorage.setItem('isAuthenticated', 'true');
        this.router.navigate(['/']);
      })
      .catch(error => {
        // Fehlerbehandlung
        console.error('Fehler bei der Anmeldung:', error);
        this.errorMessage = 'Falsches Passwort oder Benutzername!';
      });
  }
}
