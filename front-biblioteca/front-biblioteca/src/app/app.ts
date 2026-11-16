import { Component, inject, signal } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive, Router } from '@angular/router';
import { AuthService } from './services/auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('front-biblioteca');
  private readonly router = inject(Router);
  private readonly authService = inject(AuthService);

  cerrarSesion(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  mostrarNavegacion(): boolean {
    return !this.router.url.startsWith('/login');
  }
}


