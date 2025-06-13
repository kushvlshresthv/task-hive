import { Component, Input } from '@angular/core';
import { Project } from '../../new-project/new-project.model';
import { InviteMembersComponent } from './invite-members/invite-members.component';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-project',
  standalone: true,
  imports: [InviteMembersComponent],
  templateUrl: './project.component.html',
  styleUrl: './project.component.scss',
})
export class ProjectComponent {
  @Input() project!: Project;
  currentPath!: string;

  constructor(routes: ActivatedRoute) {
    routes.url.subscribe(urlSegments=> {
      this.currentPath = urlSegments.map(segment=>segment.path).join('/');
    })
  }
}
