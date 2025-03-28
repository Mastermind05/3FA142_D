import { Injectable, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CreatedialogComponent } from '../createdialog/createdialog.component';

@Injectable({
  providedIn: 'root'
})
export class DialogService {
  private dialog = inject(MatDialog);
  openDialog(): Promise<any> {
    const dialogRef = this.dialog.open(CreatedialogComponent);

    // Rückgabe eines Promises, um die Daten nach `afterClosed()` zu senden
    return dialogRef.afterClosed().toPromise();
  }
}
