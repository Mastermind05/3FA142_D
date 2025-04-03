import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatereadingdialogComponent } from './createreadingdialog.component';

describe('CreatereadingdialogComponent', () => {
  let component: CreatereadingdialogComponent;
  let fixture: ComponentFixture<CreatereadingdialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreatereadingdialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreatereadingdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
