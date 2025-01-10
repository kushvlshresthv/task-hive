import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { catchError, of } from 'rxjs';
import { BACKEND_URL } from '../global.constants';
import { Response } from '../model/response';
import { checkUsernameFormat } from '../validators';

interface Error {
  username: string[];
  password: string[];
}

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  router = inject(Router);
  http = inject(HttpClient);

  formData = new FormGroup({
    username: new FormControl('', {
      validators: [Validators.required, checkUsernameFormat],
    }),
    password: new FormControl('', {
      validators: Validators.required,
    }),
  });

  username!: FormControl;
  password!: FormControl;
  ngOnInit() {
    this.username = this.formData.controls.username;
    this.password = this.formData.controls.password;
  }

  onSubmit() {
    const authenticationDetails = `${this.username.value}:${this.password.value}`;

    const encodedAuthenticationDetails = btoa(authenticationDetails);

    const headers = new HttpHeaders({
      Authorization: `Basic ${encodedAuthenticationDetails}`,
    });

    console.log('submitting request to: ' + BACKEND_URL + '/login');
    console.log(this.router);
    this.http
      .get<Response>(BACKEND_URL + '/login', {
        headers: headers,
        withCredentials: true,
      })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          return of(null);
        }),
      )
      .subscribe({
        next: (response) => {
          if (response != null) {
            this.router.navigateByUrl('/hive');
          }
        },
      });
  }
}
