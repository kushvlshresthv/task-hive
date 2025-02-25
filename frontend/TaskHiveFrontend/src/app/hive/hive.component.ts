import { Component, ElementRef, viewChild, ViewChild } from '@angular/core';
import { MenuComponent } from './menu/menu.component';
import { NewProjectComponent } from './new-project/new-project.component';
import { RouterOutlet } from '@angular/router';
import { InboxesComponent } from './inboxes/inboxes.component';

@Component({
  selector: 'app-hive',
  standalone: true,
  imports: [
    MenuComponent,
    NewProjectComponent,
    RouterOutlet,
    InboxesComponent,
    InboxesComponent,
  ],
  templateUrl: './hive.component.html',
  styleUrl: './hive.component.scss',
})
export class HiveComponent {}
