import { HttpClient } from '@angular/common/http';
import { Component, inject } from '@angular/core';
import { BACKEND_URL } from '../global.constants';

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [],
  templateUrl: './test.component.html',
  styleUrl: './test.component.scss',
})
export class TestComponent {
  http = inject(HttpClient);
  ngOnInit() {
    this.http
      .get(`${BACKEND_URL}/test`, {
        withCredentials: true,
      })
      .subscribe({
        next: (response) => {},
      });
  }
}
