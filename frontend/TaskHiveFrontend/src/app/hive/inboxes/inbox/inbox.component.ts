import { Component, Input } from '@angular/core';
import { Inbox } from './inbox.model';

@Component({
  selector: 'app-inbox',
  standalone: true,
  imports: [],
  templateUrl: './inbox.component.html',
  styleUrl: './inbox.component.scss',
})
export class InboxComponent {
  @Input({ required: true }) inbox!: Inbox;
}
