import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CategoriaService } from '../../../services/categoria';

@Component({ selector: 'app-categoria-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, RouterLink], templateUrl: './categoria-form.html', styleUrl: './categoria-form.css' })
export class CategoriaForm {
  private readonly service = inject(CategoriaService);
  private readonly router = inject(Router);
  guardando = false;
  error = '';
  form = new FormGroup({
    nombre: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    descripcion: new FormControl('', { nonNullable: true }),
    tipo: new FormControl('', { nonNullable: true }),
    estado: new FormControl('Activa', { nonNullable: true, validators: [Validators.required] }),
  });

  guardar(): void {
    if (this.form.invalid || this.guardando) { this.form.markAllAsTouched(); return; }
    this.guardando = true;
    this.service.crear(this.form.getRawValue()).subscribe({
      next: () => this.router.navigate(['/categorias']),
      error: () => { this.error = 'No se pudo guardar la categoría.'; this.guardando = false; },
    });
  }
}
