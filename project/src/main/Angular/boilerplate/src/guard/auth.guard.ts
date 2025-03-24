import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const isAuthenticated = sessionStorage.getItem('isAuthenticated') === 'true';

  console.log('Auth Guard prüft:', isAuthenticated); // 🛠 Debugging: Zeigt, ob Auth erkannt wird

  if (isAuthenticated) {
    return true;
  } else {
    console.log('Nicht eingeloggt! Leite weiter auf /login'); // 🛠 Debugging
    router.navigate(['/login']);
    return false;
  }
};
