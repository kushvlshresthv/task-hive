import {
  Component,
  effect,
  ElementRef,
  EventEmitter,
  Output,
  viewChild,
  ViewChild,
} from '@angular/core';

@Component({
  selector: 'app-new-project',
  standalone: true,
  imports: [],
  templateUrl: './new-project.component.html',
  styleUrl: './new-project.component.scss',
})
export class NewProjectComponent {
  diag = viewChild<ElementRef<HTMLDialogElement>>('new_project_dialogue');
  diagOpen = false;

  onNewProject() {
    if (!this.diagOpen) {
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
}
