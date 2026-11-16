import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Usuario {
  id?: number;
  nombre: string;
  apellido: string;
  dni: string;
  correo: string;
  telefono: string;
  rol?: string;
}

export interface NuevoUsuario extends Usuario {
  password: string;
}

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/usuarios';

  listar(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.apiUrl);
  }

  crear(usuario: NuevoUsuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario);
  }
}
