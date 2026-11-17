
import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {

  private authService = inject(AuthService);
  private router = inject(Router);

  correo = '';
  password = '';

  mensajeError = '';
  mensajeExito = '';
  cargando = false;

  iniciarSesion(): void {

    this.mensajeError = '';
    this.mensajeExito = '';

    if (!this.correo || !this.password) {
      this.mensajeError = 'Ingrese su correo y contraseña';
      return;
    }

    this.cargando = true;

    this.authService.login(this.correo, this.password)
      .subscribe({
        next: (respuesta) => {
          this.mensajeExito = 'Inicio de sesión exitoso';
          this.cargando = false;

          console.log('Token recibido:', respuesta.token);

          // Redirigir al Dashboard
          this.router.navigate(['/dashboard']);
        },
        error: (error) => {
          this.mensajeError =
            error.error?.mensaje || 'Credenciales incorrectas';

          this.cargando = false;
        }
      });
  }
}
