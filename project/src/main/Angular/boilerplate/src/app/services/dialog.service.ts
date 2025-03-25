import { Injectable, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CreatedialogComponent } from '../createdialog/createdialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  private dialog = inject(MatDialog);

  openDialog() {
    this.dialog.open(CreatedialogComponent);
  }
  constructor() { }
}
