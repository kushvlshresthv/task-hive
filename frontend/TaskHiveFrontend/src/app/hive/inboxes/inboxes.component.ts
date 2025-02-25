import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { BACKEND_URL } from '../../global.constants';
import { Inbox } from './inbox/inbox.model';
import { Response } from '../../GLOBAL_MODEL/response';
import { InboxComponent } from './inbox/inbox.component';

@Component({
  selector: 'app-inboxes',
  standalone: true,
  imports: [InboxComponent],
  templateUrl: './inboxes.component.html',
  styleUrl: './inboxes.component.scss',
})
export class InboxesComponent {
  http = inject(HttpClient);
  inboxes!: Inbox[];
  inboxOpenFlag = false;

  onInboxOpen() {
    this.inboxOpenFlag = true;
    this.http
      .get<Response<Inbox[]>>(`${BACKEND_URL}/getInboxes`, {
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {
          this.inboxes = response.mainBody;
          console.log(this.inboxes);
        },
      });
  }

  onInboxClose() {
    this.inboxOpenFlag = false;
  }
}
