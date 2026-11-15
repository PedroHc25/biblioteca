import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { LibroList } from './components/libros/libro-list/libro-list';
import { LibroForm } from './components/libros/libro-form/libro-form';
import { CategoriaForm } from './components/categorias/categoria-form/categoria-form';
import { UsuarioForm } from './components/usuarios/usuario-form/usuario-form';
import { PrestamoForm } from './components/prestamos/prestamo-form/prestamo-form';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'libros', component: LibroList },
  { path: 'libros/nuevo', component: LibroForm },
  { path: 'libros/editar/:id', component: LibroForm }, 
  { path: 'categorias/nueva', component: CategoriaForm },
  { path: 'usuarios/nuevo', component: UsuarioForm },
  { path: 'prestamos/nuevo', component: PrestamoForm }
];
