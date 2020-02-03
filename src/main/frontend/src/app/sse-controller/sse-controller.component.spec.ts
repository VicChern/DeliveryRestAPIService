import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SseControllerComponent } from './sse-controller.component';

describe('SseControllerComponent', () => {
  let component: SseControllerComponent;
  let fixture: ComponentFixture<SseControllerComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SseControllerComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SseControllerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
