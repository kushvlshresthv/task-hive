import {
  checkIfSameValue,
  checkUsernameAvailability,
} from './signup.validators';
import { AfterViewInit, Component, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { Router, RouterLink } from '@angular/router';
import { Response } from '../GLOBAL_MODEL/response';

@Component({
  selector: 'app-signup',
  standalone: true,
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './signup.component.html',
  styleUrl: './signup.component.scss',
})
export class SignupComponent implements OnInit {
  //storing form controls in a variable for reusability in validation checking
  firstName!: FormControl;
  lastName!: FormControl;
  username!: FormControl;
  email!: FormControl;
  password!: FormControl;
  confirmPassword!: FormControl;
  pass!: FormGroup;

  private http = inject(HttpClient);
  private router = inject(Router);
  private checkUsernameAvailabilitInstance = inject(checkUsernameAvailability);

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

     //NOTE: if we don't bind the 'this' keyword to the callback function passed to asyncValidators property, it won't be able to use instance/class specific properties such as HttpClient(which is injected in the class)

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

        //NOTE: the following function call returns a function which accepts AbstractControl as argument

        //the arguments 'password' and 'confirmPassword' will still be available to the returned function due to 'closure' property of JS

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
      .post<Response<Object>>('http://localhost:8080/register', requestBody)
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.log('error message: ' + error.error.message);
          return of(null);
        }),
      )
      .subscribe({
        next: (response) => {
          if (response != null) {
            this.router.navigateByUrl('hive');
          }
        },
      });
  }
}
