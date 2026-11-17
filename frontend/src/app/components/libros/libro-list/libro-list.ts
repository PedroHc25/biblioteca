import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Libro, LibroService } from '../../../services/libro';

@Component({
  selector: 'app-libro-list',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './libro-list.html',
  styleUrl: './libro-list.css',
})
export class LibroList implements OnInit {
  private readonly libroService = inject(LibroService);
  libros: Libro[] = [];
  cargando = true;
  error = '';

  ngOnInit(): void {
    if (typeof window !== 'undefined') this.cargarLibros();
  }

  cargarLibros(): void {
    this.cargando = true;
    this.error = '';
    this.libroService.listar().subscribe({
      next: (libros) => { this.libros = libros; this.cargando = false; },
      error: () => { this.error = 'No se pudieron cargar los libros. Verifica que Java esté activo.'; this.cargando = false; },
    });
  }
}
