import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Autor {
  id?: number;
  nombreCompleto: string;
  nacionalidad: string;
  estado: string;
}

@Injectable({ providedIn: 'root' })
export class AutorService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl =
    'http://localhost:8081/api/autores';

  listar(): Observable<Autor[]> {
    return this.http.get<Autor[]>(this.apiUrl);
  }

  obtener(id: number): Observable<Autor> {
    return this.http.get<Autor>(
      `${this.apiUrl}/${id}`
    );
  }

  crear(autor: Autor): Observable<Autor> {
    return this.http.post<Autor>(
      this.apiUrl,
      autor
    );
  }

  actualizar(id: number, autor: Autor): Observable<Autor> {
    return this.http.put<Autor>(
      `${this.apiUrl}/${id}`,
      autor
    );
  }
}