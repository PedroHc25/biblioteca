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
import { forkJoin, of } from 'rxjs';

import { AuthService } from '../../../services/auth';
import {
  Autor,
  AutorService
} from '../../../services/autor';
import {
  Categoria,
  CategoriaService
} from '../../../services/categoria';
import {
  LibroRequest,
  LibroService
} from '../../../services/libro';

@Component({
  selector: 'app-libro-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterLink
  ],
  templateUrl: './libro-form.html',
  styleUrl: './libro-form.css'
})
export class LibroForm implements OnInit {

  private readonly libroService = inject(LibroService);
  private readonly autorService = inject(AutorService);
  private readonly categoriaService =
    inject(CategoriaService);

  private readonly authService = inject(AuthService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);

  readonly id =
    Number(this.route.snapshot.paramMap.get('id')) || null;

  autores: Autor[] = [];
  categorias: Categoria[] = [];

  cargando = true;
  guardando = false;
  error = '';

  form = new FormGroup({
    titulo: new FormControl('', {
      nonNullable: true,
      validators: [
        Validators.required,
        Validators.pattern(/.*\S.*/)
      ]
    }),

    autorId: new FormControl<number | null>(
      null,
      Validators.required
    ),

    categoria: new FormControl('', {
      nonNullable: true,
      validators: [Validators.required]
    }),

    anio: new FormControl<number | null>(
      null,
      {
        validators: [
          Validators.required,
          Validators.min(0),
          Validators.max(2100)
        ]
      }
    ),

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

    this.cargarDatos();
  }

  cargarDatos(): void {
    const libroRequest = this.id
      ? this.libroService.obtener(this.id)
      : of(null);

    forkJoin({
      categorias: this.categoriaService.listar(),
      autores: this.autorService.listar(),
      libro: libroRequest
    }).subscribe({
      next: (data) => {
        this.categorias = data.categorias;

        this.autores = data.autores.filter(
          (autor) =>
            autor.estado === 'Activo'
            || autor.id === data.libro?.autor?.id
        );

        if (data.libro) {
          this.form.patchValue({
            titulo: data.libro.titulo,
            autorId: data.libro.autor.id,
            categoria: data.libro.categoria,
            anio: data.libro.anio,
            estado: data.libro.estado
          });
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

        this.error = 'No se pudieron cargar los datos del libro.';
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

    const libro =
      this.form.getRawValue() as LibroRequest;

    const request = this.id
      ? this.libroService.actualizar(this.id, libro)
      : this.libroService.crear(libro);

    request.subscribe({
      next: () => {
        this.router.navigate(['/libros']);
      },

      error: (response) => {
        this.guardando = false;

        if (response.status === 401) {
          this.authService.logout();
          this.router.navigate(['/login']);
          return;
        }

        this.error = response.error?.message
          || 'No se pudo guardar el libro.';
      }
    });
  }
}