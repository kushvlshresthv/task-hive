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
  @Output() dialogClicked = new EventEmitter<undefined>();
  constructor() {
    effect(() => {
      this.diag()?.nativeElement.showModal();
    });
  }

  onDiaglogClick() {
    this.diag()?.nativeElement.close();
    this.dialogClicked.emit();
  }
}
