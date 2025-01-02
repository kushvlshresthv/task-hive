import { Component, ElementRef, ViewChild } from '@angular/core';
import { MenuComponent } from './menu/menu.component';
import { NewProjectComponent } from './new-project/new-project.component';

@Component({
  selector: 'app-hive',
  standalone: true,
  imports: [MenuComponent, NewProjectComponent],
  templateUrl: './hive.component.html',
  styleUrl: './hive.component.scss',
})
export class HiveComponent {
  newProject = false;

  onNewProject() {
    this.newProject = !this.newProject;
  }

  onDialogClicked() {
    this.newProject = !this.newProject;
  }
}
