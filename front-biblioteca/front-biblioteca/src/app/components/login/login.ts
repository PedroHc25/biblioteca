import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  loginForm = new FormGroup({
    correo: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required])
  });

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private router: Router, private authService: AuthService) {}

onLogin() {
    if (this.loginForm.valid) {
      const correo = this.loginForm.get('correo')?.value || '';
      const password = this.loginForm.get('password')?.value || '';

      this.authService.login({ email: correo, password }).subscribe({
        next: (response: any) => {
          if (response && response.token) {
            this.errorMessage = '';
            this.router.navigate(['/libros']);
          } else {
            this.errorMessage = 'El servidor no devolvió un token de acceso';
            this.successMessage = '';
          }
        },
        error: () => {
          this.errorMessage = 'Usuario o contraseña incorrectos';
          this.successMessage = '';
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
      this.errorMessage = 'Por favor completa todos los campos';
      this.successMessage = '';
    }
  }

  onRegistro() {
    this.errorMessage = '';
    this.successMessage = 'Función de registro disponible (simulado)';
    setTimeout(() => this.successMessage = '', 3000);
  }
}
