import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import { AuthService } from '../../../services/auth';
import {
  Autor,
  AutorService
} from '../../../services/autor';

@Component({
  selector: 'app-autor-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './autor-list.html',
  styleUrl: './autor-list.css'
})
export class AutorList implements OnInit {

  private readonly service = inject(AutorService);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  autores: Autor[] = [];
  cargando = true;
  error = '';

  ngOnInit(): void {
    if (typeof window === 'undefined') {
      return;
    }

    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
      return;
    }

    this.cargar();
  }

  cargar(): void {
    this.cargando = true;
    this.error = '';

    this.service.listar().subscribe({
      next: (autores) => {
        this.autores = autores;
        this.cargando = false;
      },

      error: (response) => {
        this.cargando = false;

        if (response.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          return;
        }

        this.error = 'No se pudieron cargar los autores.';
      }
    });
  }
}