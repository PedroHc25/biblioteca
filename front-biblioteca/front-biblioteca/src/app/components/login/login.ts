import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormGroup, FormControl, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  private readonly TEST_USERS = [
    { usuario: "admin", password: "12345", rol: "ADMIN" },
    { usuario: "usuario", password: "12345", rol: "USUARIO" }
  ];

  loginForm = new FormGroup({
    usuario: new FormControl('', [Validators.required, Validators.minLength(3)]),
    password: new FormControl('', [Validators.required, Validators.minLength(4)])
  });

  errorMessage: string = '';
  successMessage: string = '';

  constructor(private router: Router) {}

  onLogin() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.errorMessage = 'Por favor completa los campos correctamente';
      return;
    }

    const { usuario, password } = this.loginForm.value;
    const user = this.TEST_USERS.find(
      u => u.usuario === usuario && u.password === password
    );

    if (user) {
      localStorage.setItem('usuarioLogueado', JSON.stringify(user));
      localStorage.setItem('jwt_token', 'token-test-simulado');

      this.successMessage = '¡Bienvenido, ' + user.usuario + '!';
      this.errorMessage = '';

      setTimeout(() => {
        if (user.rol === 'ADMIN') {
          this.router.navigate(['/libros']);
        } else {
          this.router.navigate(['/home']);
        }
      }, 1000);
    } else {
      this.errorMessage = 'Usuario o contraseña incorrectos';
      this.successMessage = '';
    }
  }

  onRegistro() {
    this.errorMessage = '';
    this.successMessage = 'Función de registro disponible (simulado)';
    setTimeout(() => this.successMessage = '', 3000);
  }
}