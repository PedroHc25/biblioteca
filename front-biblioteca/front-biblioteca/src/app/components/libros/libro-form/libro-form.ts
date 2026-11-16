import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Categoria, CategoriaService } from '../../../services/categoria';
import { Libro, LibroService } from '../../../services/libro';

@Component({
  selector: 'app-libro-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './libro-form.html',
  styleUrl: './libro-form.css',
})
export class LibroForm implements OnInit {
  private readonly libroService = inject(LibroService);
  private readonly categoriaService = inject(CategoriaService);
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  readonly id = Number(this.route.snapshot.paramMap.get('id')) || null;
  categorias: Categoria[] = [];
  guardando = false;
  error = '';
  form = new FormGroup({
    titulo: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    autor: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    categoria: new FormControl('', { nonNullable: true }),
    anio: new FormControl<number | null>(null),
    estado: new FormControl('Disponible', { nonNullable: true, validators: [Validators.required] }),
  });

  ngOnInit(): void {
    if (typeof window === 'undefined') return;
    this.categoriaService.listar().subscribe({ next: (items) => this.categorias = items });
    if (this.id) {
      this.libroService.obtener(this.id).subscribe({
        next: (libro) => this.form.patchValue(libro),
        error: () => this.error = 'No se encontró el libro solicitado.',
      });
    }
  }

  guardar(): void {
    if (this.form.invalid || this.guardando) {
      this.form.markAllAsTouched();
      return;
    }
    this.guardando = true;
    this.error = '';
    const libro = this.form.getRawValue() as Libro;
    const request = this.id
      ? this.libroService.actualizar(this.id, libro)
      : this.libroService.crear(libro);
    request.subscribe({
      next: () => this.router.navigate(['/libros']),
      error: () => { this.error = 'No se pudo guardar el libro.'; this.guardando = false; },
    });
  }
}
