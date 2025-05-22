import { Component, inject, input, OnDestroy, OnInit  } from '@angular/core';
import {Router} from '@angular/router';
import { Project } from '../new-project/new-project.model';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { BACKEND_URL } from '../../global.constants';
import { Response } from '../../GLOBAL_MODEL/response';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Subscription } from 'rxjs';
import { AppComponent } from '../../app.component';
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

  constructor(private router: Router, private inboxesService:InboxesService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe({
      next: (params) => {
        this.pid = params.get("pid")!;
        this.inboxId = params.get("inboxId")!;
        console.log(this.pid);
        const reqHeaders = new HttpHeaders({
          pid: this.pid,
          inboxId : this.inboxId,
        });

        this.http
          .get<Response<Project>>(`${BACKEND_URL}/getInvitedProjectById`, {
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
    const headers = new HttpHeaders({
      "pid": this.pid + "",
      "inboxId": this.inboxId + "",
    });

    this.subscription = this.http.get<Response<Object>>(`${BACKEND_URL}/api/invitedProjects/acceptProjectInvite`, {
      withCredentials: true,
      headers: headers,
    }).subscribe({
      next:(response)=> {
        AppComponent.globalPopup.activatePopup(response.message, "success");
         this.router.navigate(['/hive/joinedProjects'], {
          relativeTo: null,
        });
      },
      error:(error)=> {
        AppComponent.globalPopup.activatePopup(error.error.message, "error")
      },
      complete:()=> {
        this.inboxesService.deleteInbox(this.inboxId);
      }
    });
  }


  onReject() {
    const headers = new HttpHeaders({
      "pid": this.pid + "",
      "inboxId":this.inboxId+"",
    });

    this.subscription = this.http.get<Response<Object>>(`${BACKEND_URL}/api/invitedProjects/rejectProjectInvite`, {
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
        this.inboxesService.deleteInbox(this.inboxId);
        this.router.navigate(['/hive/projects'], {
          relativeTo: null,
        });
      }
    });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }
}
