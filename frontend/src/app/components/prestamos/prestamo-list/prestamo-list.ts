import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin } from 'rxjs';
import { Libro, LibroService } from '../../../services/libro';
import { Prestamo, PrestamoService } from '../../../services/prestamo';
import { Usuario, UsuarioService } from '../../../services/usuario';

@Component({ selector: 'app-prestamo-list', standalone: true, imports: [CommonModule, RouterLink], templateUrl: './prestamo-list.html', styleUrl: './prestamo-list.css' })
export class PrestamoList implements OnInit {
  private readonly prestamoService = inject(PrestamoService);
  private readonly usuarioService = inject(UsuarioService);
  private readonly libroService = inject(LibroService);
  prestamos: Prestamo[] = [];
  usuarios: Usuario[] = [];
  libros: Libro[] = [];
  cargando = true;
  error = '';
  ngOnInit(): void { if (typeof window !== 'undefined') this.cargar(); }
  cargar(): void {
    this.cargando = true;
    this.error = '';
    forkJoin({ prestamos: this.prestamoService.listar(), usuarios: this.usuarioService.listar(), libros: this.libroService.listar() }).subscribe({
      next: (data) => { this.prestamos = data.prestamos; this.usuarios = data.usuarios; this.libros = data.libros; this.cargando = false; },
      error: () => { this.error = 'No se pudieron cargar los préstamos. Verifica tu sesión y que Java esté activo.'; this.cargando = false; },
    });
  }
  nombreUsuario(id: number): string { const usuario = this.usuarios.find((item) => item.id === id); return usuario ? `${usuario.nombre} ${usuario.apellido}` : `Usuario #${id}`; }
  tituloLibro(id: number): string { return this.libros.find((item) => item.id === id)?.titulo ?? `Libro #${id}`; }
}
