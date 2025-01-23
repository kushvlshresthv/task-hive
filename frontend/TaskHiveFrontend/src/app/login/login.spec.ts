import { provideHttpClient } from '@angular/common/http';
import { LoginComponent } from './login.component';
import { TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { BACKEND_URL } from '../global.constants';

describe('login component test', () => {
  let router: jasmine.SpyObj<Router>;
  let login: LoginComponent;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    router = jasmine.createSpyObj('Router', ['navigateByUrl']);
    router.navigateByUrl.and.returnValue(new Promise(() => {}));

    TestBed.configureTestingModule({
      providers: [
        LoginComponent,
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router, useValue: router },
      ],
    });

    login = TestBed.inject(LoginComponent);
    httpTestingController = TestBed.inject(HttpTestingController);

    login.username = new FormControl('username');
    login.password = new FormControl('password');
  });

  it('[LoginComponent] onSubmit() should navigate to /hive ', () => {
    login.onSubmit();

    const request = httpTestingController.expectOne(`${BACKEND_URL}/login`);

    request.flush({
      message: 'success',
      object: null,
    });

    expect(router.navigateByUrl.calls.count()).toEqual(1);
  });

  it('[LoginComponent] onSubmit() should not do anything due to HttpError response', () => {
    login.onSubmit();
    const request = httpTestingController.expectOne(`${BACKEND_URL}/login`);

    const errorEvent = new ErrorEvent('API ERROR');
    const status = 500;
    const statusText = 'Internal Server Error';

    request.error(errorEvent, {
      status: status,
      statusText: statusText,
    });

    expect(router.navigateByUrl.calls.count()).toEqual(0);
  });
});
