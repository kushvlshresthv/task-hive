import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { debounceTime, map, Observable, of } from 'rxjs';
import { Response } from '../GLOBAL_MODEL/response';
import { BACKEND_URL } from '../global.constants';

export function checkIfSameValue(field1: string, field2: string) {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    const field1value = control.get(field1)?.value;
    const field2value = control.get(field2)?.value;
    if (field1value === field2value) {
      return of(null);
    } else {
      return of({
        notSameValue: `${field1} and ${field2} are not same`,
      });
    }
  };
}

@Injectable({
  providedIn: 'root',
})

//async validator which returns a null observable if the username is available
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
        .post<
          Response<Object>
        >(`${BACKEND_URL}/checkUsernameAvailability`, control.value)
        .pipe(
          debounceTime(1000),
          map((response) => {
            if (response.message == 'true') {
              return null;
            } else {
              return { usernameNotAvailable: 'username is not available' };
            }
          }),
        );
    }

    //this line is never executed as one of the above observable is returned not the following
    return of(null);
  }
}
