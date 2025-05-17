import { Injectable } from '@angular/core';
import { Inbox } from './inbox/inbox.model';

@Injectable({
  providedIn: 'root'
})
export class InboxesService {
  inboxes: Inbox[] = [];

  getInboxes():Inbox[] {
    return this.inboxes;
  }

  setInboxes(inboxes: Inbox[]) {
    this.inboxes = [...inboxes];
  }

  addInbox(inbox:Inbox) {
    this.inboxes = [...this.inboxes, inbox];
  }

}
