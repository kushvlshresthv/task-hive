import { Component, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { checkUsernameFormat } from '../validators';

interface Error {
  username: string[];
  password: string[];
}

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  standalone: true,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  errors: Error = {
    username: [],
    password: [],
  };

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

  checkIfUsernameNotValid(): boolean {
    this.errors.username.length = 0;
    let flag = false;
    if (this.username.touched && this.username.dirty) {
      if (this.username.errors?.['required']) {
        this.errors.username[0] = 'Username is required';
        flag = true;
      }

      if (this.username.errors?.['usernameImproperFormat']) {
        this.errors.username[1] =
          'Username must not have any spaces or special symbols';
        flag = true;
      }
    }
    return flag;
  }

  checkIfPasswordNotValid(): boolean {
    this.errors.password.length = 0;
    let flag = false;
    if (this.password.touched && this.password.dirty) {
      if (this.password.errors?.['required']) {
        this.errors.password[0] = 'Password is required';
        flag = true;
      }
    }

    return flag;
  }

  onSubmit() {}
}
