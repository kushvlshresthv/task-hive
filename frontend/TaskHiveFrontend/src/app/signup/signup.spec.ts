import { Router } from '@angular/router';
import { SignupComponent } from './signup.component';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { FormControl } from '@angular/forms';
import { BACKEND_URL } from '../global.constants';

describe('signup component test', () => {
  let router: jasmine.SpyObj<Router>;
  let signup: SignupComponent;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    router = jasmine.createSpyObj('Router', ['navigateByUrl']);
    router.navigateByUrl.and.returnValue(new Promise(() => {}));
    TestBed.configureTestingModule({
      providers: [
        SignupComponent,
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: Router, useValue: router },
      ],
    });

    signup = TestBed.inject(SignupComponent);
    httpTestingController = TestBed.inject(HttpTestingController);

    signup.firstName = new FormControl('firstName');
    signup.lastName = new FormControl('lastName');
    signup.username = new FormControl('username');
    signup.email = new FormControl('email');
    signup.password = new FormControl('password');
    signup.confirmPassword = new FormControl('confirmPassword');
  });

  it('[SignupComponent] onSubmit() should navigate to /hive', () => {
    signup.onSubmit();

    const request = httpTestingController.expectOne(`${BACKEND_URL}/register`);

    request.flush({
      message: 'success',
      object: null,
    });

    expect(router.navigateByUrl.calls.count()).toEqual(1);
  });

  it('[SignupComponent] onSubmit() should not do anything due to HttpError response', () => {
    signup.onSubmit();
    const request = httpTestingController.expectOne(`${BACKEND_URL}/register`);

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
