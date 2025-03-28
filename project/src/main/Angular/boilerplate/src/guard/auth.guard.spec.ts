import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { authGuard } from './auth.guard';
import { ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

describe('authGuard', () => {
  let routerSpy: jasmine.SpyObj<Router>;

  beforeEach(() => {
    routerSpy = jasmine.createSpyObj<Router>('Router', ['navigate']); // Mock Router mit `navigate()`

    TestBed.configureTestingModule({
      imports: [RouterTestingModule],
      providers: [{ provide: Router, useValue: routerSpy }] // Bereitstellen des gemockten Routers
    });
  });

  it('should allow navigation when authenticated', () => {
    localStorage.setItem('isAuthenticated', 'true'); // Setze Auth-Status

    const next: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot;
    const state: RouterStateSnapshot = { url: '/home' } as RouterStateSnapshot;

    expect(authGuard(next, state)).toBeTrue(); // Sollte Zugriff erlauben
  });

  it('should deny navigation and redirect when not authenticated', () => {
    localStorage.removeItem('isAuthenticated'); // Stelle sicher, dass der User nicht eingeloggt ist

    const next: ActivatedRouteSnapshot = {} as ActivatedRouteSnapshot;
    const state: RouterStateSnapshot = { url: '/home' } as RouterStateSnapshot;

    expect(authGuard(next, state)).toBeFalse(); // Sollte Zugriff verweigern
    expect(routerSpy.navigate).toHaveBeenCalledWith(['/login']); // Sollte zur Login-Seite weiterleiten
  });
});
