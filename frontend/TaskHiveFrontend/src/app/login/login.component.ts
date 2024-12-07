import { Component } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  formData = new FormGroup({
    username: new FormControl('', {
      validators: Validators.required,
    }),
    password: new FormControl('', {
      validators: Validators.required,
    }),
  });
  onSubmit() {}
}
