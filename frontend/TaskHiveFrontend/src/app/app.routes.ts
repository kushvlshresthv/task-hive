import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HiveComponent } from './hive/hive.component';
import { isAuthenticated } from './app.guards';
import { ErrorComponent } from './error/error.component';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'hive',
    pathMatch: 'full',
  },
  {
    path: 'login',
    component: LoginComponent,
  },

  {
    path: 'signup',
    component: SignupComponent,
  },

  {
    path: 'hive',
    component: HiveComponent,
    canMatch: [isAuthenticated],
  },

  {
    path: '**',
    component: ErrorComponent,
  },
];
