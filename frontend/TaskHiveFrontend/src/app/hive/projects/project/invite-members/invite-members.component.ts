import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { checkUsernameAvailability } from './invite-members.validator';
import { HttpClient } from '@angular/common/http';
import { BACKEND_URL } from '../../../../global.constants';
import { Response } from '../../../../model/response';
@Component({
  selector: 'app-invite-members',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './invite-members.component.html',
  styleUrl: './invite-members.component.scss',
})
export class InviteMembersComponent {
  private checkUsernameAvailabilityInstance = inject(checkUsernameAvailability);
  @Input() pid!: number | null;
  private http = inject(HttpClient);
  form = new FormGroup({
    username: new FormControl('', {
      validators: Validators.required,
      asyncValidators: [
        this.checkUsernameAvailabilityInstance.validate.bind(
          this.checkUsernameAvailabilityInstance,
        ),
      ],
    }),
  });

  onSubmit() {
    console.log(this.pid);
    console.log(this.form.controls.username.value);

    this.http
      .post<Response>(
        `${BACKEND_URL}/addUserToProject`,
        {
          username: this.form.controls.username.value,
          pid: this.pid,
        },
        {
          withCredentials: true,
        },
      )
      .subscribe({
        next: (response) => {
          console.log(response.message);
        },
      });
  }
}
