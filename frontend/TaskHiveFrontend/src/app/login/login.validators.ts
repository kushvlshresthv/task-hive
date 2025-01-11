import { AbstractControl } from '@angular/forms';

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
