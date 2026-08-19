import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StartAiChatComponent } from './start-ai-chat.component';

describe('StartAiChatComponent', () => {
  let component: StartAiChatComponent;
  let fixture: ComponentFixture<StartAiChatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StartAiChatComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StartAiChatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
