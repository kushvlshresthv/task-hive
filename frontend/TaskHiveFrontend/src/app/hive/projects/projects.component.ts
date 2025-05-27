import { HttpClient } from '@angular/common/http';
import { Component, inject, OnInit } from '@angular/core';
import { BACKEND_URL } from '../../global.constants';
import { Project } from '../new-project/new-project.model';
import { ProjectComponent } from './project/project.component';
import { Response } from '../../GLOBAL_MODEL/response';
import { ActivatedRoute } from '@angular/router';


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
  currentPath!:string;


  ngOnInit(): void {
    if(this.currentPath == "projects") {
      this.http
        .get<Response<Project[]>>(`${BACKEND_URL}/api/projects`, {
          withCredentials: true,
        })
        .subscribe({
          next: (response) => {
            this.projects = response.mainBody;
          },
        });
    } else if(this.currentPath = "joinedProjects") {
      this.http .get<Response<Project[]>>(`${BACKEND_URL}/api/getJoinedProjects`, {
          withCredentials: true,
        })
        .subscribe({
          next: (response) => {
            this.projects = response.mainBody;
          },
        });
      }
  }



  constructor(routes: ActivatedRoute) {
      routes.url.subscribe(urlSegments=> {
      this.currentPath = urlSegments.map(segment => segment.path).join('/');
      console.log('Loaded via path:', this.currentPath);
      })
  }
}
