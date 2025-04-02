import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdatereadingdialogComponent } from './updatereadingdialog.component';

describe('UpdatereadingdialogComponent', () => {
  let component: UpdatereadingdialogComponent;
  let fixture: ComponentFixture<UpdatereadingdialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdatereadingdialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdatereadingdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
