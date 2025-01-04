import {
  Component,
  effect,
  ElementRef,
  EventEmitter,
  Output,
  viewChild,
  ViewChild,
} from '@angular/core';
import {
  FormControl,
  FormGroup,
  Validators,
  FormsModule,
  ReactiveFormsModule,
} from '@angular/forms';

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

  formData = new FormGroup({
    projectName: new FormControl('', {
      validators: [Validators.required],
    }),
    projectDescription: new FormControl('', {}),
    startDate: new FormControl('', {
      validators: [Validators.required],
    }),
    dueDate: new FormControl('', {
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
    console.log(this.formData.controls.projectName.value);
    console.log(this.formData.controls.projectDescription.value);
    console.log(this.formData.controls.startDate.value);
    console.log(this.formData.controls.dueDate.value);
    console.log(this.formData.controls.priority.value);
    console.log(this.formData.controls.projectType.value);
  }
}
