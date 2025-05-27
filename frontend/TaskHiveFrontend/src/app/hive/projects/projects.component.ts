import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { BACKEND_URL } from '../../global.constants';
import { Project } from '../new-project/new-project.model';
import { ProjectComponent } from './project/project.component';

interface Response {
  message: string | null;
  mainBody: Project[];
}

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [ProjectComponent],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss',
})
export class ProjectsComponent implements OnInit {
  private http = inject(HttpClient);
  projects!: Project[] | null;
  ngOnInit(): void {
    this.http
      .get<Response>(`${BACKEND_URL}/api/projects`, {
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          this.projects = response.mainBody;
        },
      });
  }
}
