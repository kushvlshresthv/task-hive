import { Component, inject, input, OnDestroy, OnInit } from '@angular/core';
import { Project } from '../new-project/new-project.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BACKEND_URL } from '../../global.constants';
import { Response } from '../../GLOBAL_MODEL/response';
import { RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-invited-projects',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './invited-projects.component.html',
  styleUrl: './invited-projects.component.scss',
})
export class InvitedProjectsComponent implements OnInit, OnDestroy {
  //initial project object to render in the template till http request completes
  //removing this causes error while the template tries to read from undefined 'project' object

  project = new Project();

  http = inject(HttpClient);
  pid = input.required<string>();
  subscription?: Subscription;

  ngOnInit(): void {
    console.log(this.pid());

    const reqHeaders = new HttpHeaders({
      pid: this.pid(),
    });

    this.http
      .get<Response<Project>>(`${BACKEND_URL}/getProjectById`, {
        headers: reqHeaders,
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          this.project = response.mainBody;
        },
      });
  }


  onAccept() {
    console.log("on accept called");
    const headers = new HttpHeaders({
      "pid": this.pid() + "",
    });

    this.subscription = this.http.get<Response<Object>>(`${BACKEND_URL}/api/invitedProjects/acceptProjectInvite`, {
      withCredentials: true,
      headers: headers,
    }).subscribe({
      next:(response)=> {
        console.log(response);
      },
      error:(error)=> {
        console.log(error);
      }
    });
  }


  onReject() {
    const headers = new HttpHeaders({
      "pid": this.pid() + "",
    });

    this.subscription = this.http.get<Response<Object>>(`${BACKEND_URL}/rejectProjectinvite`, {
      withCredentials: true,
      headers: headers,
    }).subscribe();


  }

  ngOnDestroy() {
    this.subscription?.unsubscribe()
  }
}
