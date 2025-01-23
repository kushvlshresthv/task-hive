import { Component, ElementRef, viewChild, ViewChild } from '@angular/core';
import { MenuComponent } from './menu/menu.component';
import { NewProjectComponent } from './new-project/new-project.component';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-hive',
  standalone: true,
  imports: [MenuComponent, NewProjectComponent, RouterOutlet],
  templateUrl: './hive.component.html',
  styleUrl: './hive.component.scss',
})
export class HiveComponent {}
