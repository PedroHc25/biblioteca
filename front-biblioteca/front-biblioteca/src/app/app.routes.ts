import { Routes } from '@angular/router';
import { Login } from './components/login/login';
import { LibroList } from './components/libros/libro-list/libro-list';
import { LibroForm } from './components/libros/libro-form/libro-form';
import { CategoriaForm } from './components/categorias/categoria-form/categoria-form';
import { UsuarioForm } from './components/usuarios/usuario-form/usuario-form';
import { PrestamoForm } from './components/prestamos/prestamo-form/prestamo-form';
import { CategoriaList } from './components/categorias/categoria-list/categoria-list';
import { UsuarioList } from './components/usuarios/usuario-list/usuario-list';
import { PrestamoList } from './components/prestamos/prestamo-list/prestamo-list';
import { authGuard } from './guards/auth-guard';
import { AutorList } from './components/autores/autor-list/autor-list';
import { AutorForm } from './components/autores/autor-form/autor-form';
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'libros', component: LibroList },
  { path: 'libros/nuevo', component: LibroForm, canActivate: [authGuard] },
  { path: 'libros/editar/:id', component: LibroForm,  canActivate: [authGuard] }, 
  { path: 'categorias', component: CategoriaList, canActivate: [authGuard] },
  { path: 'categorias/nueva', component: CategoriaForm, canActivate: [authGuard] },
  { path: 'categorias/editar/:id', component: CategoriaForm, canActivate: [authGuard]},
  { path: 'usuarios', component: UsuarioList },
  { path: 'usuarios/nuevo', component: UsuarioForm },
  { path: 'prestamos', component: PrestamoList, canActivate: [authGuard]}, 
  { path: 'prestamos/nuevo', component: PrestamoForm, canActivate: [authGuard] },
  { path: 'prestamos/editar/:id', component: PrestamoForm, canActivate: [authGuard]},
  { path: 'autores', component: AutorList, canActivate: [authGuard]},
  { path: 'autores/nuevo', component: AutorForm, canActivate: [authGuard]},
  { path: 'autores/editar/:id', component: AutorForm, canActivate: [authGuard]},
  { path: '**', redirectTo: 'login' }
];
