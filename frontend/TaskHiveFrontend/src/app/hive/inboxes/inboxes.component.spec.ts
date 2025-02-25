import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InboxesComponent } from './inboxes.component';

describe('InboxesComponent', () => {
  let component: InboxesComponent;
  let fixture: ComponentFixture<InboxesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InboxesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InboxesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
