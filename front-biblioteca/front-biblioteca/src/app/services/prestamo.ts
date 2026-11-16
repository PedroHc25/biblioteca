import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Prestamo {
  id?: number;
  fechaPrestamo: string;
  fechaDevolucion: string;
  estado: string;
  usuarioId: number;
  libroId: number;
}

@Injectable({ providedIn: 'root' })
export class PrestamoService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8081/api/prestamos';

  listar(): Observable<Prestamo[]> {
    return this.http.get<Prestamo[]>(this.apiUrl);
  }

  crear(prestamo: Prestamo): Observable<Prestamo> {
    return this.http.post<Prestamo>(this.apiUrl, prestamo);
  }
}
