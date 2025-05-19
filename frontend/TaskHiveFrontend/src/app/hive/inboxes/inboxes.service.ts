import { Injectable, signal, WritableSignal } from '@angular/core';
import { Inbox } from './inbox/inbox.model';

@Injectable({
  providedIn: 'root'
})
export class InboxesService {
  private inboxes = signal<Inbox[]>([]);

  getInboxes():WritableSignal<Inbox[]> {
    return this.inboxes;
  }

  setInboxes(inboxes: Inbox[]) {
    this.inboxes.set([...inboxes]);
  }

  addInbox(inbox:Inbox) {
    this.inboxes.set([...this.inboxes(), inbox]);
  }

  deleteInbox(inboxId:string) {
    this.inboxes.set(this.inboxes().filter(inbox=>inbox.inboxId != inboxId));
  }

}
