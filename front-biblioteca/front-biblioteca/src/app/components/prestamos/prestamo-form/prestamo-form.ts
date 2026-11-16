import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Libro, LibroService } from '../../../services/libro';
import { PrestamoService } from '../../../services/prestamo';
import { Usuario, UsuarioService } from '../../../services/usuario';

@Component({ selector: 'app-prestamo-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, RouterLink], templateUrl: './prestamo-form.html', styleUrl: './prestamo-form.css' })
export class PrestamoForm implements OnInit {
  private readonly prestamoService = inject(PrestamoService);
  private readonly usuarioService = inject(UsuarioService);
  private readonly libroService = inject(LibroService);
  private readonly router = inject(Router);
  usuarios: Usuario[] = [];
  libros: Libro[] = [];
  cargando = true;
  guardando = false;
  error = '';
  form = new FormGroup({
    usuarioId: new FormControl<number | null>(null, Validators.required),
    libroId: new FormControl<number | null>(null, Validators.required),
    fechaPrestamo: new FormControl(new Date().toISOString().slice(0, 10), { nonNullable: true, validators: [Validators.required] }),
    fechaDevolucion: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
  });

  ngOnInit(): void {
    if (typeof window === 'undefined') return;
    forkJoin({ usuarios: this.usuarioService.listar(), libros: this.libroService.listar() }).subscribe({
      next: (data) => { this.usuarios = data.usuarios; this.libros = data.libros; this.cargando = false; },
      error: () => { this.error = 'No se pudieron cargar usuarios y libros. Verifica tu sesión.'; this.cargando = false; },
    });
  }

  guardar(): void {
    if (this.form.invalid || this.guardando) { this.form.markAllAsTouched(); return; }
    this.guardando = true;
    this.error = '';
    const value = this.form.getRawValue();
    this.prestamoService.crear({
      usuarioId: value.usuarioId!, libroId: value.libroId!,
      fechaPrestamo: value.fechaPrestamo, fechaDevolucion: value.fechaDevolucion,
      estado: 'Pendiente',
    }).subscribe({
      next: () => this.router.navigate(['/prestamos']),
      error: () => { this.error = 'No se pudo registrar el préstamo.'; this.guardando = false; },
    });
  }
}
