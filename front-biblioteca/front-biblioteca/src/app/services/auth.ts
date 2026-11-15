import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = 'http://localhost:8080/api/auth';

  login(credenciales: { username: string; password: string }): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credenciales).pipe(
      tap(response => {
        if (response.token) {
          localStorage.setItem('jwt_token', response.token);
        }
      })
    );
  }

  obtenerToken(): string | null {
    return localStorage.getItem('jwt_token');
  }
}
