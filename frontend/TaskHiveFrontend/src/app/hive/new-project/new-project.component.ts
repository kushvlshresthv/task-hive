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
    startDate: new FormControl('', {
      validators: [Validators.required],
    }),
    finishDate: new FormControl('', {
      validators: [Validators.required],
    }),
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

  onSubmit() {
    const newProject: Project = {
      projectName: this.formData.controls.projectName.value,
      projectDescription: this.formData.controls.projectDescription.value,
      startDate: this.formData.controls.startDate.value,
      finishDate: this.formData.controls.finishDate.value,
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
