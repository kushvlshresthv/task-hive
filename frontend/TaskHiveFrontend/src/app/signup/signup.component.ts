import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormControlName,
  FormGroup,
  FormGroupName,
  ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { checkIfSameValue, checkUsernameAvailability } from '../validators';
import { FormControlErrorMessages } from './signup.model';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
})
export class SignupComponent implements OnInit {
  //storing form controls in a variable for reusability in validation checking
  private firstName!: FormControl;
  private lastName!: FormControl;
  private username!: FormControl;
  private email!: FormControl;
  private password!: FormControl;
  private confirmPassword!: FormControl;
  private pass!: FormGroup;

  private http = inject(HttpClient);
  private checkUsernameAvailabilitInstance = inject(checkUsernameAvailability);
  errorMessages: FormControlErrorMessages = {
    firstName: [],
    lastName: [],
    username: [],
    email: [],
    password: [],
    confirmPassword: [],
  };

  formData = new FormGroup({
    name: new FormGroup({
      firstName: new FormControl('', {
        validators: [Validators.required],
      }),
      lastName: new FormControl('', {
        validators: [Validators.required],
      }),
    }),
    username: new FormControl('', {
      validators: [Validators.required],
      asyncValidators: [
        this.checkUsernameAvailabilitInstance.validate.bind(
          this.checkUsernameAvailabilitInstance,
        ),
      ],
      updateOn: 'blur',
    }),
    email: new FormControl('', {
      validators: [Validators.required, Validators.email],
      updateOn: 'blur',
    }),
    pass: new FormGroup(
      {
        password: new FormControl('', {
          validators: [Validators.required, Validators.minLength(5)],
        }),
        confirmPassword: new FormControl('', {
          validators: [Validators.required],
        }),
      },
      {
        asyncValidators: checkIfSameValue('password', 'confirmPassword'),
      },
    ),
  });

  ngOnInit(): void {
    this.firstName = this.formData.controls.name.controls.firstName;
    this.lastName = this.formData.controls.name.controls.lastName;
    this.username = this.formData.controls.username;
    this.email = this.formData.controls.email;
    this.password = this.formData.controls.pass.controls.password;
    this.confirmPassword = this.formData.controls.pass.controls.confirmPassword;
    this.pass = this.formData.controls.pass;
  }

  //returns true if control is invalid and inserts the error message in the 'errorMessages'
  checkIfFirstNameNotValid() {
    let flag = false;
    this.errorMessages.firstName.length = 0;
    if (this.firstName.touched && this.firstName.dirty) {
      if (this.firstName.errors?.['required']) {
        this.errorMessages.firstName[0] = 'First name is required';
        flag = true;
      }
    }
    return flag;
  }

  checkIfLastNameNotValid() {
    let flag = false;
    this.errorMessages.lastName.length = 0;
    if (this.lastName.touched && this.lastName.dirty) {
      if (this.lastName.errors?.['required']) {
        this.errorMessages.lastName[0] = 'Last name is required';
        flag = true;
      }
    }
    return flag;
  }

  checkIfUsernameNotValid() {
    let flag = false;
    this.errorMessages.username.length = 0;
    if (this.username.touched && this.username.dirty) {
      if (this.username.errors?.['required']) {
        this.errorMessages.username[0] = 'Username is required';
        flag = true;
      }
      if (this.username.errors?.['usernameImproperFormat']) {
        this.errorMessages.username[1] =
          'Username must not have any spaces or special symbols';
        flag = true;
      }
      if (this.username.errors?.['usernameNotAvailable']) {
        this.errorMessages.username[2] = 'This username is already taken';
        flag = true;
      }
    }
    return flag;
  }

  checkIfEmailNotValid() {
    let flag = false;
    this.errorMessages.email.length = 0;
    if (this.email.touched && this.email.dirty) {
      if (this.email.errors?.['required']) {
        this.errorMessages.email[0] = 'Email is required';
        flag = true;
      }

      if (this.email.errors?.['email']) {
        this.errorMessages.email[1] = 'Enter a valid email';
        flag = true;
      }
    }

    return flag;
  }

  checkIfPasswordNotValid() {
    let flag = false;
    this.errorMessages.password.length = 0;
    if (this.password.touched && this.password.dirty) {
      if (this.password.errors?.['required']) {
        this.errorMessages.password[0] = 'Password is required';
        flag = true;
      }

      if (this.password.errors?.['minlength'] != null) {
        this.errorMessages.password[2] =
          'Password must be at least 5 letters long';
        flag = true;
      }
      if (this.pass.errors?.['notSameValue']) {
        flag = true;
      }
    }

    return flag;
  }

  checkIfConfirmPasswordNotValid() {
    let flag = false;
    this.errorMessages.confirmPassword.length = 0;
    if (this.confirmPassword.touched && this.confirmPassword.dirty) {
      if (this.confirmPassword.errors?.['required']) {
        this.errorMessages.confirmPassword[0] = 'Confirm Password is required';
        flag = true;
      }
      if (this.pass.errors?.['notSameValue']) {
        this.errorMessages.confirmPassword[1] =
          'Password and Confirm Password fields do not match';
        flag = true;
      }
    }

    return flag;
  }

  onSubmit() {
    const requestBody = {
      firstName: this.formData.controls.name.controls.firstName.value,
      lastName: this.lastName.value,
      username: this.username.value,
      email: this.email.value,
      password: this.password.value,
      confirmPassword: this.confirmPassword.value,
    };
    this.formData.reset();
    this.http
      .post<{ message: string }>('http://localhost:8080/register', requestBody)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.log('error message: ' + error.error.message);
          return of(null);
        }),
      )
      .subscribe({
        next: (value) => {
          if (value != null) {
            console.log(value.message);
          }
        },
      });
  }
}
