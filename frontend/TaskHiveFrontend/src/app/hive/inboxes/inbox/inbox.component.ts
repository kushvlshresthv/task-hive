import { Component, Input } from '@angular/core';
import { Inbox } from './inbox.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-inbox',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './inbox.component.html',
  styleUrl: './inbox.component.scss',
})
export class InboxComponent {
  @Input({ required: true }) inbox!: Inbox;
}
