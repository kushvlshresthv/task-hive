import { Component, inject, input, OnDestroy, OnInit } from '@angular/core';
import { Project } from '../new-project/new-project.model';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { BACKEND_URL } from '../../global.constants';
import { Response } from '../../GLOBAL_MODEL/response';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { AppComponent } from '../../app.component';
import { Inbox } from '../inboxes/inbox/inbox.model';
import { InboxesService } from '../inboxes/inboxes.service';

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
  pid!:string;
  subscription?: Subscription;
  inboxId!:string;

  constructor(private inboxesService:InboxesService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe({
      next: (params) => {
        this.pid = params.get("pid")!;
        this.inboxId = params.get("inboxId")!;
        console.log(this.pid);
        const reqHeaders = new HttpHeaders({
          pid: this.pid,
        });

        this.http
          .get<Response<Project>>(`${BACKEND_URL}/getProjectById`, {
            headers: reqHeaders,
            withCredentials: true,
          })
          .subscribe({
            next: (response) => {
              this.project = response.mainBody;
              AppComponent.globalPopup.activatePopup(response.message, "success");
            },
            error: (error: HttpErrorResponse) => {
              AppComponent.globalPopup.activatePopup(error.error.message, "error");
            }
          });
      }
    })
  }





  onAccept() {
    console.log("on accept called");
    const headers = new HttpHeaders({
      "pid": this.pid + "",
      "inboxId": this.inboxId + "",
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
      },
      complete:()=> {
        console.log(this.inboxesService.getInboxes()());
        this.inboxesService.deleteInbox(this.inboxId);
        console.log("complete() is run");
      }
    });
  }


  onReject() {
    const headers = new HttpHeaders({
      "pid": this.pid + "",
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
