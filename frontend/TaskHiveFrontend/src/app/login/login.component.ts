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
import { catchError, config, of, Subscription } from 'rxjs';
import { BACKEND_URL } from '../global.constants';
import { Response } from '../GLOBAL_MODEL/response';
import { checkUsernameFormat } from './login.validators';
import { AppComponent } from '../app.component';

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
  subscription!: Subscription;
  popup = AppComponent.globalPopup;

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
    this.subscription = this.http
      .get<Response<Object>>(BACKEND_URL + '/login', {
        headers: headers,
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          console.log('LOG: ' + response.message);
          this.popup.activatePopup(response.message, 'success');
          this.router.navigateByUrl('/hive');
        },
        error: (error) => {
          console.log(error.error.message);
          this.popup.activatePopup(error.error.message, 'error');
          this.router.navigateByUrl('/login');
        },
      });
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
