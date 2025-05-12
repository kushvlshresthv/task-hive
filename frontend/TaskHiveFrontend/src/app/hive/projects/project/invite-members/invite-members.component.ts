import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { checkUsernameAvailability } from './invite-members.validator';
import {
  HttpClient,
  HttpErrorResponse,
  HttpHeaders,
} from '@angular/common/http';
import { BACKEND_URL } from '../../../../global.constants';
import { Response } from '../../../../GLOBAL_MODEL/response';
import { catchError, of } from 'rxjs';
import { AppComponent } from '../../../../app.component';
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
  popup = AppComponent.globalPopup;

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
    let httpHeader = new HttpHeaders();
    httpHeader = httpHeader.append('pid', `${this.pid}`);
    httpHeader = httpHeader.append(
      'username',
      `${this.form.controls.username.value}`,
    );

    this.http
      .get<Response<Object>>(`${BACKEND_URL}/createProjectInvite`, {
        headers: httpHeader,
        withCredentials: true,
      })
      .pipe(
        catchError((errorObj: HttpErrorResponse) => {
          this.popup.activatePopup(errorObj.error.message, 'error');
          return of(null);
        }),
      )
      .subscribe({
        next: (response) => {
          if (response != null) {
            this.popup.activatePopup(response.message, 'success');
          }
        },
      });
  }
}
