import { HttpClient } from '@angular/common/http';
import { Component, effect, inject } from '@angular/core';
import { BACKEND_URL } from '../../global.constants';
import { Inbox } from './inbox/inbox.model';
import { Response } from '../../GLOBAL_MODEL/response';
import { InboxComponent } from './inbox/inbox.component';
import { InboxesService } from './inboxes.service';
import { map } from 'rxjs';

@Component({
  selector: 'app-inboxes',
  standalone: true,
  imports: [InboxComponent],
  templateUrl: './inboxes.component.html',
  styleUrl: './inboxes.component.scss',
})
export class InboxesComponent {
  http = inject(HttpClient);
  inboxOpenFlag = false;
  inboxes!:Inbox[];

  constructor(private inboxesService:InboxesService) {
    effect(()=> {
      this.inboxes = inboxesService.getInboxes()();
    })
  }

  onInboxOpen() {
    this.inboxOpenFlag = true;
    this.http
      .get<Response<Inbox[]>>(`${BACKEND_URL}/getInboxes`, {
        withCredentials: true,
      }).pipe(map((response)=> {
        response.mainBody.forEach((inbox)=>{inbox.createdDate=new Date(inbox.createdDate)});
        return response;
      }))
      .subscribe({
        next: (response) => {
          this.inboxesService.setInboxes(response.mainBody);
        },
      });
  }

  onInboxClose() {
    this.inboxOpenFlag = false;
  }
}
