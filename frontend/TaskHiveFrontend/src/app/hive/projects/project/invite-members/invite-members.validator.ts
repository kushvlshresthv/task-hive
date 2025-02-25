import { debounceTime, map, Observable, of } from 'rxjs';
import { BACKEND_URL } from '../../../../global.constants';
import {
  AbstractControl,
  AsyncValidator,
  ValidationErrors,
} from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Response } from '../../../../GLOBAL_MODEL/response';
import { Injectable } from '@angular/core';

//validate() returns a null if the user with the given username exists
@Injectable({
  providedIn: 'root',
})
export class checkUsernameAvailability implements AsyncValidator {
  constructor(private http: HttpClient) {}

  /*
  HOW THIS WORKS: ASYNC VALIDATORS

  this validator is associated with a form control. When all the sync validators of the form control is executed, following validate() method is executed

  validate() method returns an observable, 

  That observable is subscribed by the form control. If the observable returns a particular value, this validation is successful

  If the observable returns an object, the validation has failed, and the returned object is then stored by the FormControl
*/

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
            //we are mapping 'false' to 'valid data' becuase we are checking username availability

            //'false' means the username is available
            if (response.message == 'false') {
              return null;
            } else {
              //if the username availability is 'true', that means the user does not exists and hence we are mapping this condition validation failure
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
