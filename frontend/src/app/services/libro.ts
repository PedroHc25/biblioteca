import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { Autor } from './autor';

export interface Libro {
  id?: number;
  titulo: string;
  autor: Autor;
  categoria: string;
  anio: number;
  estado: string;
}

export interface LibroRequest {
  titulo: string;
  autorId: number;
  categoria: string;
  anio: number;
  estado: string;
}

@Injectable({ providedIn: 'root' })
export class LibroService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl =
    'http://localhost:8081/api/libros';

  listar(): Observable<Libro[]> {
    return this.http.get<Libro[]>(this.apiUrl);
  }

  obtener(id: number): Observable<Libro> {
    return this.http.get<Libro>(
      `${this.apiUrl}/${id}`
    );
  }

  crear(libro: LibroRequest): Observable<Libro> {
    return this.http.post<Libro>(
      this.apiUrl,
      libro
    );
  }

  actualizar(
    id: number,
    libro: LibroRequest
  ): Observable<Libro> {
    return this.http.put<Libro>(
      `${this.apiUrl}/${id}`,
      libro
    );
  }
}