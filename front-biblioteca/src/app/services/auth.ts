
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface LoginRequest {
  correo: string;
  password: string;
}

interface LoginResponse {
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8081/api/auth';

  // Iniciar sesión
  login(correo: string, password: string): Observable<LoginResponse> {

    const datos: LoginRequest = { correo, password };

    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      datos
    ).pipe(
      tap(respuesta => {
        localStorage.setItem('token', respuesta.token);
      })
    );
  }

  // Cerrar sesión
  logout(): void {
    localStorage.removeItem('token');
  }

  // Verificar si existe un token
  estaAutenticado(): boolean {
    return !!localStorage.getItem('token');
  }
}
