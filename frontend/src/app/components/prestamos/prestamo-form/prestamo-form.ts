import { CommonModule } from '@angular/common';
import { Component, inject, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from '@angular/forms';
import {
  ActivatedRoute,
  Router,
  RouterLink
} from '@angular/router';
import { forkJoin, of } from 'rxjs';

import { AuthService } from '../../../services/auth';
import {
  Libro,
  LibroService
} from '../../../services/libro';
import {
  Prestamo,
  PrestamoService
} from '../../../services/prestamo';
import {
  Usuario,
  UsuarioService
} from '../../../services/usuario';

function validarFechas(
  control: AbstractControl
): ValidationErrors | null {
  const fechaPrestamo =
    control.get('fechaPrestamo')?.value;

  const fechaDevolucion =
    control.get('fechaDevolucion')?.value;

  if (
    fechaPrestamo &&
    fechaDevolucion &&
    fechaDevolucion < fechaPrestamo
  ) {
    return { fechaDevolucionInvalida: true };
  }

  return null;
}

@Component({
  selector: 'app-prestamo-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './prestamo-form.html',
  styleUrl: './prestamo-form.css'
})
export class PrestamoForm implements OnInit {

  private readonly prestamoService =
    inject(PrestamoService);

  private readonly usuarioService =
    inject(UsuarioService);

  private readonly libroService =
    inject(LibroService);

  private readonly authService =
    inject(AuthService);

  private readonly route =
    inject(ActivatedRoute);

  private readonly router =
    inject(Router);

  readonly id =
    Number(this.route.snapshot.paramMap.get('id')) || null;

  usuarios: Usuario[] = [];
  libros: Libro[] = [];

  cargando = true;
  guardando = false;
  error = '';

  form = new FormGroup(
    {
      usuarioId: new FormControl<number | null>(
        null,
        Validators.required
      ),

      libroId: new FormControl<number | null>(
        null,
        Validators.required
      ),

      fechaPrestamo: new FormControl(
        new Date().toISOString().slice(0, 10),
        {
          nonNullable: true,
          validators: [Validators.required]
        }
      ),

      fechaDevolucion: new FormControl(
        '',
        {
          nonNullable: true,
          validators: [Validators.required]
        }
      ),

      estado: new FormControl(
        'Pendiente',
        {
          nonNullable: true,
          validators: [Validators.required]
        }
      )
    },
    { validators: validarFechas }
  );

  ngOnInit(): void {
    if (typeof window === 'undefined') {
      return;
    }

    if (!this.authService.estaLogueado()) {
      this.router.navigate(['/login']);
      return;
    }

    this.cargarDatos();
  }

  cargarDatos(): void {
    const prestamoRequest = this.id
      ? this.prestamoService.obtener(this.id)
      : of(null);

    forkJoin({
      usuarios: this.usuarioService.listar(),
      libros: this.libroService.listar(),
      prestamo: prestamoRequest
    }).subscribe({
      next: (data) => {
        this.usuarios = data.usuarios;

        // En un préstamo nuevo solo se muestran
        // libros disponibles.
        this.libros = data.libros.filter(
          (libro) =>
            libro.estado === 'Disponible'
            || libro.id === data.prestamo?.libroId
        );

        if (data.prestamo) {
          this.form.patchValue(data.prestamo);

          // Usuario y libro no se cambian al editar.
          this.form.controls.usuarioId.disable();
          this.form.controls.libroId.disable();
        }

        this.cargando = false;
      },

      error: (response) => {
        this.cargando = false;

        if (response.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          return;
        }

        this.error = this.id
          ? 'No se pudo cargar el préstamo solicitado.'
          : 'No se pudieron cargar usuarios y libros.';
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

    const prestamo =
      this.form.getRawValue() as Prestamo;

    const request = this.id
      ? this.prestamoService.actualizar(
          this.id,
          prestamo
        )
      : this.prestamoService.crear(prestamo);

    request.subscribe({
      next: () => {
        this.router.navigate(['/prestamos']);
      },

      error: (response) => {
        this.guardando = false;

        if (response.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          return;
        }

        if (response.status === 400) {
          this.error = response.error?.message
            || 'Verifica las fechas y la disponibilidad del libro.';
          return;
        }

        this.error = this.id
          ? 'No se pudo actualizar el préstamo.'
          : 'No se pudo registrar el préstamo.';
      }
    });
  }
}