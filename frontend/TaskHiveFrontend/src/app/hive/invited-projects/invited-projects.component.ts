import { Component, inject, input, OnInit } from '@angular/core';
import { Project } from '../new-project/new-project.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BACKEND_URL } from '../../global.constants';
import { Response } from '../../GLOBAL_MODEL/response';

@Component({
  selector: 'app-invited-projects',
  standalone: true,
  imports: [],
  templateUrl: './invited-projects.component.html',
  styleUrl: './invited-projects.component.scss'
})
export class InvitedProjectsComponent implements OnInit{

  //initial project object to render in the template till http request completes
  //removing this causes error while the template tries to read from undefined 'project' object

  project: Project = {
    pid: 0,
    projectName: "",
    projectDescription:"",
    startDate: "",
    finishDate: "",
    priority: "",
    projectType: "",
  }

  http = inject(HttpClient);
  pid = input.required<string>();


  ngOnInit():void {
    console.log(this.pid());

    const reqHeaders = new HttpHeaders({
      pid: this.pid()
    });

    this.http.get<Response<Project>>(`${BACKEND_URL}/getProjectById`
    , {
      headers: reqHeaders,
      withCredentials:true,
    }).subscribe({
      next:(response) => {
        this.project = response.mainBody
      }
    }
    );
  }
}
