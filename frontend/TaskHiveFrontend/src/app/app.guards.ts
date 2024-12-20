import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Route, Router, UrlSegment } from '@angular/router';
import { Response } from './model/response';
import { BACKEND_URL } from './global.constants';
import { catchError, map, Observable, of } from 'rxjs';

export function isAuthenticated(route: Route, segment: UrlSegment[]) {
  let result: Observable<boolean> = of(false);
  const http = inject(HttpClient);
  const router = inject(Router);
  http
    .get<Response>(BACKEND_URL + '/isAuthenticated', {
      withCredentials: true,
    })
    .pipe(
      map((response) => {
        if (response?.message == 'true') {
          return of(true);
        } else return router.parseUrl('/error');
      }),
      catchError((error: HttpErrorResponse) => {
        return of(null);
      }),
    );
}
