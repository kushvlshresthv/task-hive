import {
  Component,
  effect,
  ElementRef,
  inject,
  viewChild,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { Project } from './new-project.module';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { BACKEND_URL } from '../../global.constants';
import { Response } from '../../model/response';
import { isFuture, isFinishDateLaterThanStartDate } from '../../validators';

@Component({
  selector: 'app-new-project',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './new-project.component.html',
  styleUrl: './new-project.component.scss',
})
export class NewProjectComponent {
  diag = viewChild<ElementRef<HTMLDialogElement>>('new_project_dialogue');
  diagOpen = false;
  http = inject(HttpClient);

  formData = new FormGroup({
    projectName: new FormControl('', {
      validators: [Validators.required],
    }),
    projectDescription: new FormControl('', {}),
    dates: new FormGroup(
      {
        startDate: new FormControl('', {
          validators: [Validators.required, isFuture],
        }),
        finishDate: new FormControl('', {
          validators: [Validators.required, isFuture],
        }),
      },
      {
        validators: [isFinishDateLaterThanStartDate],
      },
    ),
    priority: new FormControl('', {
      validators: [Validators.required],
    }),
    projectType: new FormControl('', {
      validators: [Validators.required],
    }),
  });

  constructor() {
    effect(() => {
      this.diag()!.nativeElement.showModal();
    });
  }
  onNewProject() {
    if (!this.diagOpen) {
      this.diagOpen = !this.diagOpen;
      this.diag()!.nativeElement.showModal();
    }
  }

  onDiaglogClick($event: MouseEvent) {
    const rect = this.diag()!.nativeElement.getBoundingClientRect();

    if (
      $event.clientX < rect.left ||
      $event.clientX > rect.right ||
      $event.clientY < rect.top ||
      $event.clientY > rect.bottom
    ) {
      console.log(rect.left);
      console.log(rect.right);
      console.log(rect.bottom);
      console.log(rect.top);

      console.log($event.clientX);
      console.log($event.clientY);
      this.diagOpen = false;
      this.diag()!.nativeElement.close();
    }
  }

  checkIfProjectNameIsNotValid(): boolean {
    const projectName = this.formData.controls.projectName;
    if (projectName.touched && projectName.dirty) {
      if (projectName.errors?.['required']) {
        return true;
      }
      return false;
    }
    return false;
  }

  checkIfStartDateIsNotValid(): string | undefined {
    const startDate = this.formData.controls.dates.controls.startDate;
    if (startDate.touched) {
      if (startDate.errors?.['required']) {
        return 'Select start date';
      }
    }

    //kept outside of 'touched' check because if i select a date in the past, the error message is not displayed immediately.
    if (startDate.errors?.['notFutureDate'] && !startDate.pristine) {
      return 'Select a future date';
    }
    return undefined;
  }

  checkIfFinishDateIsNotValid(): string | undefined {
    const finishDate = this.formData.controls.dates.controls.finishDate;
    if (finishDate.touched) {
      if (finishDate.errors?.['required']) {
        return 'Select finish date';
      }
    }
    if (finishDate.errors?.['notFutureDate'] && !finishDate.pristine) {
      return 'Select a future date';
    }
    return undefined;
  }

  checkIfDatesAreNotValid() {
    const dates = this.formData.controls.dates;
    if (
      dates.touched &&
      !dates.controls.finishDate.errors?.['notFutureDate'] &&
      !dates.controls.startDate.errors?.['notFutureDate']
    ) {
      if (dates.errors?.['improperDates']) {
        return true;
      }
    }
    return false;
  }
  onSubmit() {
    const newProject: Project = {
      projectName: this.formData.controls.projectName.value,
      projectDescription: this.formData.controls.projectDescription.value,
      startDate: this.formData.controls.dates.controls.startDate.value,
      finishDate: this.formData.controls.dates.controls.finishDate.value,
      priority: this.formData.controls.priority.value,
      projectType: this.formData.controls.projectType.value,
    };

    this.formData.reset();
    this.http
      .post<Response>(BACKEND_URL + '/createProject', newProject, {
        withCredentials: true,
      })
      .pipe(
        catchError((error: HttpErrorResponse) => {
          console.log('error message: ' + error.error.message);
          return of(null);
        }),
      )
      .subscribe({
        next: (response) => {
          if (response != null) {
            console.log(response.message);
          }
        },
      });
  }
}
