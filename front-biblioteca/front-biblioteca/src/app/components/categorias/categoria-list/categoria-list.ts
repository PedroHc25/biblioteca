import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Categoria, CategoriaService } from '../../../services/categoria';

@Component({ selector: 'app-categoria-list', standalone: true, imports: [CommonModule, RouterLink], templateUrl: './categoria-list.html', styleUrl: './categoria-list.css' })
export class CategoriaList implements OnInit {
  private readonly service = inject(CategoriaService);
  categorias: Categoria[] = [];
  cargando = true;
  error = '';
  ngOnInit(): void { if (typeof window !== 'undefined') this.cargar(); }
  cargar(): void {
    this.cargando = true;
    this.service.listar().subscribe({
      next: (items) => { this.categorias = items; this.cargando = false; },
      error: () => { this.error = 'No se pudieron cargar las categorías. Verifica que Java esté activo.'; this.cargando = false; },
    });
  }
}
