import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { SignupComponent } from './signup/signup.component';
import { HiveComponent } from './hive/hive.component';
import { invitedProjectsGuard, isAuthenticated } from './app.guards';
import { ErrorComponent } from './error/error.component';
import { InvitedProjectsComponent } from './hive/invited-projects/invited-projects.component';
import { ProjectsComponent } from './hive/projects/projects.component';
import { TestComponent } from './test/test.component';

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
    path: 'register',
    component: SignupComponent,
  },

  {
    path: 'hive',
    component: HiveComponent,
    canMatch: [isAuthenticated],
    children: [
      {
        path: 'projects',
        component: ProjectsComponent,
      },
      {
        path: 'joinedProjects',
        component: ProjectsComponent,
      },

      {
          path: 'invitedProjects',
          component: InvitedProjectsComponent,
          canActivate: [invitedProjectsGuard],
      },
    ],
  },

  {
    path: 'test',
    component: TestComponent,
    canMatch: [isAuthenticated],
  },

  {
    path: '**',
    component: ErrorComponent,
  },
];
