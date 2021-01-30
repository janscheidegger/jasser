import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ChooseTrumpComponent } from './choose-trump.component';

describe('ChooseTrumpComponent', () => {
  let component: ChooseTrumpComponent;
  let fixture: ComponentFixture<ChooseTrumpComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ChooseTrumpComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChooseTrumpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
