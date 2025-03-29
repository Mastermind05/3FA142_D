import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingdialogComponent } from './settingdialog.component';

describe('SettingdialogComponent', () => {
  let component: SettingdialogComponent;
  let fixture: ComponentFixture<SettingdialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SettingdialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SettingdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
