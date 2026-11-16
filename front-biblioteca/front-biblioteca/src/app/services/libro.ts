import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Libro {
  id?: number;
  titulo: string;
  autor: string;
  categoria: string;
  anio: number | null;
  estado: string;
}

@Injectable({ providedIn: 'root' })
export class LibroService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/libros';

  listar(): Observable<Libro[]> {
    return this.http.get<Libro[]>(this.apiUrl);
  }

  obtener(id: number): Observable<Libro> {
    return this.http.get<Libro>(`${this.apiUrl}/${id}`);
  }

  crear(libro: Libro): Observable<Libro> {
    return this.http.post<Libro>(this.apiUrl, libro);
  }

  actualizar(id: number, libro: Libro): Observable<Libro> {
    return this.http.put<Libro>(`${this.apiUrl}/${id}`, libro);
  }
}
