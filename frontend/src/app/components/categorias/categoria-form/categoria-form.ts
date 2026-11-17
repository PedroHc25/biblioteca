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
  Categoria,
  CategoriaService
} from '../../../services/categoria';

@Component({
  selector: 'app-categoria-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './categoria-form.html',
  styleUrl: './categoria-form.css'
})
export class CategoriaForm implements OnInit {

  private readonly service = inject(CategoriaService);
  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly id =
    Number(this.route.snapshot.paramMap.get('id')) || null;

  guardando = false;
  cargando = false;
  error = '';

  form = new FormGroup({
    nombre: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/.*\S.*/)
      ]
    }),

    descripcion: new FormControl('', {
      nonNullable: true
    }),

    tipo: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/.*\S.*/)
      ]
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
      this.cargarCategoria();
    }
  }

  cargarCategoria(): void {
    if (!this.id) {
      return;
    }

    this.cargando = true;
    this.error = '';

    this.service.obtener(this.id).subscribe({
      next: (categoria) => {
        this.form.patchValue(categoria);
        this.cargando = false;
      },
      error: () => {
        this.error = 'No se encontró la categoría solicitada.';
        this.cargando = false;
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

    const categoria =
      this.form.getRawValue() as Categoria;

    const request = this.id
      ? this.service.actualizar(this.id, categoria)
      : this.service.crear(categoria);

    request.subscribe({
      next: () => {
        this.router.navigate(['/categorias']);
      },
      error: () => {
        this.error = this.id
          ? 'No se pudo actualizar la categoría.'
          : 'No se pudo registrar la categoría.';

        this.guardando = false;
      }
    });
  }
}