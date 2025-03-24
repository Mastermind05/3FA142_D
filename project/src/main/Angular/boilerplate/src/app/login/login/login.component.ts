import { Component } from '@angular/core';
import { Router } from '@angular/router';

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

  login() {
    const correctPassword = '123456'; // Beispielpasswort (sollte im echten Fall nicht hart codiert sein)
    console.log(this.password)
    if (this.password === correctPassword) {
      sessionStorage.setItem('isAuthenticated', 'true'); // Benutzer als authentifiziert markieren
      this.router.navigate(['/']); // Nach erfolgreicher Anmeldung zur Startseite navigieren
      console.log(localStorage.getItem('isAuthenticated'))
    } else {
      this.errorMessage = 'Falsches Passwort!';
    }
  }
}
