import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { NuevoUsuario, UsuarioService } from '../../../services/usuario';

@Component({ selector: 'app-usuario-form', standalone: true, imports: [CommonModule, ReactiveFormsModule, RouterLink], templateUrl: './usuario-form.html', styleUrl: './usuario-form.css' })
export class UsuarioForm {
  private readonly service = inject(UsuarioService);
  private readonly router = inject(Router);
  guardando = false;
  error = '';
  form = new FormGroup({
    nombre: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    apellido: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    dni: new FormControl('', { nonNullable: true, validators: [Validators.required] }),
    correo: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.email] }),
    telefono: new FormControl('', { nonNullable: true }),
    password: new FormControl('', { nonNullable: true, validators: [Validators.required, Validators.minLength(8)] }),
  });

  guardar(): void {
    if (this.form.invalid || this.guardando) { this.form.markAllAsTouched(); return; }
    this.guardando = true;
    this.error = '';
    this.service.crear(this.form.getRawValue() as NuevoUsuario).subscribe({
      next: () => this.router.navigate(['/usuarios']),
      error: (response) => {
        this.error = response.status === 409
          ? 'El correo o DNI ya está registrado.'
          : 'No se pudo crear el usuario. Verifica los datos y que el correo y DNI sean únicos.';
        this.guardando = false;
      },
    });
  }
}
