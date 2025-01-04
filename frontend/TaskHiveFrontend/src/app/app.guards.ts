import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Route, Router, UrlSegment } from '@angular/router';
import { Response } from './model/response';
import { BACKEND_URL } from './global.constants';
import { catchError, map, Observable, of } from 'rxjs';

export function isAuthenticated(route: Route, segment: UrlSegment[]) {
  const http = inject(HttpClient);
  const router = inject(Router);
  return http
    .get<Response>(BACKEND_URL + '/isAuthenticated', {
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
