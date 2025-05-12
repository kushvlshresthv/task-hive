import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './header/header.component';
import { Popup } from './GLOBAL_MODEL/popup';
import _default from '@angular/common/locales/dz';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  title = 'Task Hive';
  static globalPopup = new Popup();
  popup = AppComponent.globalPopup;
}

//NOTE: the globalPopup can be implemented with a new PopupService (a dependency injected in the root)
