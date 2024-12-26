import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuComponent } from './menu/menu.component';
import { NewProjectComponent } from '../new-project/new-project.component';

@Component({
  selector: 'app-hive',
  standalone: true,
  imports: [MenuComponent, NewProjectComponent],
  templateUrl: './hive.component.html',
  styleUrl: './hive.component.scss',
})
export class HiveComponent {
  newProject = false;
  @ViewChild('diag') diagloue?: ElementRef<HTMLDialogElement>;

  onNewProject() {
    this.newProject = !this.newProject;
    this.diagloue?.nativeElement.show();
    console.log('button clicked' + this.diagloue);
  }

  onDialogClicked() {
    this.newProject = false;
  }
}
