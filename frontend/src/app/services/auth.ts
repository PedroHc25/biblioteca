import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators'; // <-- Importante para interceptar la respuesta y guardar datos

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8081/api/auth';

  constructor(private http: HttpClient) {}

  login(credentials: { email: string; password: string }): Observable<{ token: string; rol?: string }> {
    const payload = {
      correo: credentials.email,
      password: credentials.password
    };
    
    return this.http.post<{ token: string; rol?: string }>(`${this.apiUrl}/login`, payload).pipe(
      tap(response => {
        if (response && response.token) {
          this.setItem('jwt_token', response.token);
          if (response.rol) {
            this.setItem('user_role', response.rol);
          }
        }
      })
    );
  }

  obtenerToken(): string | null {
    return this.getItem('jwt_token');
  }

  obtenerRol(): string | null {
    return this.getItem('user_role') || 'USUARIO';
  }

  estaLogueado(): boolean {
    return !!this.obtenerToken();
  }

  logout(): void {
    this.removeItem('jwt_token');
    this.removeItem('user_role');
    this.removeItem('usuarioLogueado');
  }

  private getItem(key: string): string | null {
    return typeof localStorage === 'undefined' ? null : localStorage.getItem(key);
  }

  private setItem(key: string, value: string): void {
    if (typeof localStorage !== 'undefined') localStorage.setItem(key, value);
  }

  private removeItem(key: string): void {
    if (typeof localStorage !== 'undefined') localStorage.removeItem(key);
  }
}
