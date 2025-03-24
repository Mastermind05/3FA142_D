import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: false,
  
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  password: string = '';
  errorMessage: string = '';

  constructor(private router: Router) {}

  login() {
    const correctPassword = '123456'; // Beispielpasswort (sollte im echten Fall nicht hart codiert sein)

    if (this.password === correctPassword) {
      localStorage.setItem('isAuthenticated', 'true'); // Benutzer als authentifiziert markieren
      this.router.navigate(['/']); // Nach erfolgreicher Anmeldung zur Startseite navigieren
    } else {
      this.errorMessage = 'Falsches Passwort!';
    }
  }
}
