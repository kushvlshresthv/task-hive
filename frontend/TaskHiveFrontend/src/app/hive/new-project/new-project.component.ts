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
import { Project } from './new-project.model';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { catchError, of } from 'rxjs';
import { BACKEND_URL } from '../../global.constants';
import { Response } from '../../GLOBAL_MODEL/response';
import {
  isFuture,
  isFinishDateLaterThanStartDate,
} from './new-project.validators';

@Component({
  selector: 'app-new-project',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './new-project.component.html',
  styleUrl: './new-project.component.scss',
})
export class NewProjectComponent {
  http = inject(HttpClient);
  diag = viewChild<ElementRef<HTMLDialogElement>>('new_project_dialogue');

  //form controls
  projectName = new FormControl('', {
    validators: [Validators.required],
  });

  projectDescription = new FormControl('', {});

  startDate = new FormControl('', {
    validators: [Validators.required, isFuture],
  });
  finishDate = new FormControl('', {
    validators: [Validators.required, isFuture],
  });

  priority = new FormControl('', {
    validators: [Validators.required],
  });
  projectType = new FormControl('', {
    validators: [Validators.required],
  });

  formData = new FormGroup({
    projectName: this.projectName,
    projectDescription: this.projectDescription,
    dates: new FormGroup(
      {
        startDate: this.startDate,
        finishDate: this.finishDate,
      },
      {
        validators: [isFinishDateLaterThanStartDate],
      },
    ),
    priority: this.priority,
    projectType: this.projectType,
  });

  //enable during development of this component
  //automatically renders the dialog box when component is initialized

  // constructor() {
  //   effect(() => {
  //     this.diag()!.nativeElement.showModal();
  //   });
  // }

  //opens the dialog
  openDialog() {
    console.log('executed');
    if (!this.diag()!.nativeElement.open) {
      this.diag()!.nativeElement.showModal();
    }
  }

  //close the dialog when clicked outside of dialog's boundry
  onDiaglogClick($event: MouseEvent) {
    const rect = this.diag()!.nativeElement.getBoundingClientRect();

    if (
      $event.clientX < rect.left - 175 ||
      $event.clientX > rect.right + 175 ||
      $event.clientY < rect.top - 175 ||
      $event.clientY > rect.bottom + 175
    ) {
      console.log(rect.left);
      console.log(rect.right);
      console.log(rect.bottom);
      console.log(rect.top);

      console.log($event.clientX);
      console.log($event.clientY);
      this.diag()!.nativeElement.close();

      console.log('executed');
    }
  }

  onSubmit() {
    const newProject = new Project();
    newProject.projectName = this.projectName.value;
    newProject.projectDescription = this.projectDescription.value;
    newProject.startDate = this.startDate.value;
    newProject.finishDate = this.finishDate.value;
    newProject.priority = this.priority.value;
    newProject.projectType = this.projectType.value;
    newProject.status = 'PLANNED';

    this.formData.reset();
    this.http
      .post<Response<Object>>(BACKEND_URL + '/api/createProject', newProject, {
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
