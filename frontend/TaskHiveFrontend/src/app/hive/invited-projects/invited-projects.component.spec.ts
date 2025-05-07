import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InvitedProjectsComponent } from './invited-projects.component';

describe('InvitedProjectsComponent', () => {
  let component: InvitedProjectsComponent;
  let fixture: ComponentFixture<InvitedProjectsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InvitedProjectsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InvitedProjectsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
