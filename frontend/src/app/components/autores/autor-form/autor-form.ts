import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators
} from '@angular/forms';
import {
  ActivatedRoute,
  Router,
  RouterLink
} from '@angular/router';

import { AuthService } from '../../../services/auth';
import {
  Autor,
  AutorService
} from '../../../services/autor';

@Component({
  selector: 'app-autor-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './autor-form.html',
  styleUrl: './autor-form.css'
})
export class AutorForm implements OnInit {

  private readonly service = inject(AutorService);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly id =
    Number(this.route.snapshot.paramMap.get('id')) || null;

  cargando = false;
  guardando = false;
  error = '';

  form = new FormGroup({
    nombreCompleto: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/.*\S.*/)
      ]
    }),

    nacionalidad: new FormControl('', {
      nonNullable: true
    }),

    estado: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    })
  });

  ngOnInit(): void {
    if (typeof window === 'undefined') {
      return;
    }

    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
      return;
    }

    if (this.id) {
      this.cargarAutor();
    }
  }

  cargarAutor(): void {
    if (!this.id) {
      return;
    }

    this.cargando = true;

    this.service.obtener(this.id).subscribe({
      next: (autor) => {
        this.form.patchValue(autor);
        this.cargando = false;
      },

      error: (response) => {
        this.cargando = false;

        if (response.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          return;
        }

        this.error = 'No se encontró el autor solicitado.';
      }
    });
  }

  guardar(): void {
    if (this.form.invalid || this.guardando) {
      this.form.markAllAsTouched();
      return;
    }

    this.guardando = true;
    this.error = '';

    const autor =
      this.form.getRawValue() as Autor;

    const request = this.id
      ? this.service.actualizar(this.id, autor)
      : this.service.crear(autor);

    request.subscribe({
      next: () => {
        this.router.navigate(['/autores']);
      },

      error: (response) => {
        this.guardando = false;

        if (response.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          return;
        }

        this.error = this.id
          ? 'No se pudo actualizar el autor.'
          : 'No se pudo registrar el autor.';
      }
    });
  }
}