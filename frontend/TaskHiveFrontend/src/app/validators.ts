import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { debounceTime, delay, map, Observable, of } from 'rxjs';
import { Response } from './model/response';
import { BACKEND_URL } from './global.constants';

//being used in signup component
export function checkIfSameValue(field1: string, field2: string) {
  return (control: AbstractControl): Observable<ValidationErrors | null> => {
    const field1value = control.get(field1)?.value;
    const field2value = control.get(field2)?.value;
    if (field1value === field2value) {
      return of(null).pipe(delay(1000));
    } else {
      return of({
        notSameValue: `${field1} and ${field2} are not same`,
      }).pipe(delay(1000));
    }
  };
}

//being used in new-project component

export function isFinishDateLaterThanStartDate(control: AbstractControl) {
  const startDate = control.get('startDate')?.value;
  const finishDate = control.get('finishDate')?.value;
  const startDateObj = new Date(startDate);
  const finishDateObj = new Date(finishDate);
  if (startDateObj.getTime() > finishDateObj.getTime()) {
    return {
      improperDates: 'start date is greater than finish date',
    };
  }

  return null;
}

export function isFuture(control: AbstractControl) {
  const selectedDate = new Date(control.value);

  if (
    Date.now() > selectedDate.getTime() &&
    Date.now() - selectedDate.getTime() > 86400000 //greater than 1 day
  ) {
    console.log('returning not future date');
    return {
      notFutureDate: 'select a future date',
    };
  }
  console.log('returning null');
  return null;
}

//being used in login component
export function checkUsernameFormat(control: AbstractControl) {
  const specialSymbolRegex = /[!@#$%^&*(),.?":{}|<>\\\/\[\];'`~\-=+ ]/;
  const enteredUsername = control.value;
  const result = specialSymbolRegex.test(enteredUsername);
  if (result) {
    return {
      usernameImproperFormat: 'username is not in proper format',
    };
  } else {
    return null;
  }
}

//being used in sign up component

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
            if (response.message == 'true') {
              return null;
            } else {
              return { usernameNotAvailable: 'username is not available' };
            }
          }),
        );
    }
    return of(null);
  }
}
