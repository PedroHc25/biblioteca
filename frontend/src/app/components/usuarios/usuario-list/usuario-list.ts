import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Usuario, UsuarioService } from '../../../services/usuario';

@Component({ selector: 'app-usuario-list', standalone: true, imports: [CommonModule, RouterLink], templateUrl: './usuario-list.html', styleUrl: './usuario-list.css' })
export class UsuarioList implements OnInit {
  private readonly service = inject(UsuarioService);
  usuarios: Usuario[] = [];
  cargando = true;
  error = '';
  ngOnInit(): void { if (typeof window !== 'undefined') this.cargar(); }
  cargar(): void {
    this.cargando = true;
    this.service.listar().subscribe({
      next: (items) => { this.usuarios = items; this.cargando = false; },
      error: () => { this.error = 'No se pudieron cargar los usuarios. Verifica tu sesión y que Java esté activo.'; this.cargando = false; },
    });
  }
}
