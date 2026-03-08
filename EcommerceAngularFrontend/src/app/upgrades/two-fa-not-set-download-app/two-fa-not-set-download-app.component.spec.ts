import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TwoFaNotSetDownloadAppComponent } from './two-fa-not-set-download-app.component';

describe('TwoFaNotSetDownloadAppComponent', () => {
  let component: TwoFaNotSetDownloadAppComponent;
  let fixture: ComponentFixture<TwoFaNotSetDownloadAppComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TwoFaNotSetDownloadAppComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TwoFaNotSetDownloadAppComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
