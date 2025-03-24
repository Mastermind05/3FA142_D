import { TestBed } from '@angular/core/testing';
import { authGuard } from './auth.guard'; // Importiere den Guard
import { RouterTestingModule } from '@angular/router/testing'; // Für Routing
import { Router, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

describe('authGuard', () => {
  let guard: authGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule],  // Wir fügen RouterTestingModule hinzu, um Router zu simulieren
      providers: [authGuard]  // Wir fügen den Guard als Provider hinzu
    });

    guard = TestBed.inject(authGuard);  // Instanziiere den Guard mit TestBed
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();  // Überprüfe, ob der Guard instanziiert wurde
  });

  it('should return true from canActivate', () => {
    // Mock-Daten für `next` und `state`
    const next: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot;  // Leerer Mock für `next`
    const state: RouterStateSnapshot = { url: '/test' } as RouterStateSnapshot;  // Leerer Mock für `state`

    // Teste, ob canActivate wahr zurückgibt
    expect(guard.canActivate(next, state)).toBe(true);  // Hier erwartet man, dass true zurückgegeben wird
  });
});
