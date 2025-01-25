import { debounceTime, map, Observable, of } from 'rxjs';
import { BACKEND_URL } from '../../../../global.constants';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Response } from '../../../../model/response';
import { Injectable } from '@angular/core';

//validate() returns a null if the user with the given username exists
@Injectable({
  providedIn: 'root',
})
export class checkUsernameAvailability implements AsyncValidator {
  constructor(private http: HttpClient) {}

  validate(control: AbstractControl): Observable<ValidationErrors | null> {
    //first check if username is in proper format
    let valid = true;
    const specialSymbolRegex = /[!@#$%^&*(),.?":{}|<>\\\/\[\];'`~\-=+ ]/;
    const enteredUsername = control.value;
    const result = specialSymbolRegex.test(enteredUsername);
    if (result) {
      valid = false;
      return of({
        usernameImproperFormat: 'username is not in proper format',
      });
    }

    //then check if the username is available
    if (valid) {
      return this.http
        .post<Response>(
          `${BACKEND_URL}/checkUsernameAvailability`,
          control.value,
        )
        .pipe(
          debounceTime(1000),
          map((response) => {
            if (response.message == 'false') {
              return null;
            } else {
              return {
                userDoesNotExist: 'user with the given username does not exist',
              };
            }
          }),
        );
    }

    //this line is never executed as one of the above observable is returned not the following
    return of(null);
  }
}
