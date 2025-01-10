import { HttpClient, provideHttpClient } from '@angular/common/http';
import { LoginComponent } from './login.component';
import { of } from 'rxjs';
import { Response } from '../model/response';
import { TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { provideRouter, Router, RouterLink } from '@angular/router';
import {
  HttpClientTestingModule,
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { routes } from '../app.routes';
import { By } from '@angular/platform-browser';

describe('login component test', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideRouter(routes),
        LoginComponent,
        provideHttpClientTesting(),
        provideHttpClient(),
      ],
    });
  });

  it('just a demo', () => {
    const login = TestBed.inject(LoginComponent);
  });
});

// describe('LoginComponenTest', () => {
//   let http: jasmine.SpyObj<HttpClient>;
//   let router: jasmine.SpyObj<Router>;
//   beforeEach(() => {
//     //creating spy object
//     http = jasmine.createSpyObj('HttpClient', ['get']);
//     router = jasmine.createSpyObj('Router', ['navigateByUrl']);

//     //configuring spy objects
//     http.get.and.returnValue(of(1));
//     router.navigateByUrl.and.returnValue(new Promise(() => {}));

//     //setting up test env
//     TestBed.configureTestingModule({
//       providers: [
//         LoginComponent,
//         { provide: HttpClient, useValue: http },
//         { provide: Router, useValue: router },
//       ],
//     });
//   });

//   it('LoginComponent_OnSubmit_ReturnsRedirect', (done: DoneFn) => {
//     const login = TestBed.inject(LoginComponent);

//     login.username = new FormControl('username');
//     login.password = new FormControl('password');

//     login.onSubmit();

//     expect(http.get.calls.count())
//       .withContext('get method called once')
//       .toBe(1);

//     expect(router.navigateByUrl.calls.count())
//       .withContext('reroute to /hive')
//       .toBe(1);

//     done();
//   });
// });
