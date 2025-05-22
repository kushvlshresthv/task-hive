import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivateFn, CanMatchFn, Route, Router, RouterStateSnapshot, UrlSegment } from '@angular/router';
import { Response } from './GLOBAL_MODEL/response';
import { BACKEND_URL } from './global.constants';
import { catchError, map , of } from 'rxjs';

export function isAuthenticated(route: Route, segment: UrlSegment[]) {
  const http = inject(HttpClient);
  const router = inject(Router);
  return http
    .get<Response<Object>>(BACKEND_URL + '/isAuthenticated', {
      withCredentials: true,
    })
    .pipe(
      map((response) => {
        if (response?.message == 'true') {
          return true;
        } else return router.parseUrl('/login');
      }),
      catchError((error: HttpErrorResponse) => {
        return of(router.parseUrl('/login'));
      }),
    );
}


/* prevents InvitedProjectComponent to be loaded without the query parameters of pid and inboxID*/
export const invitedProjectsGuard: CanActivateFn= (route:ActivatedRouteSnapshot, state: RouterStateSnapshot)=> {
  const router = inject(Router);
  const inboxId = route.queryParams['inboxId'];
  const pid = route.queryParams['pid'];
  if (inboxId && pid) {
    return true;
  }
  return router.parseUrl('/error'); //redirect
};
