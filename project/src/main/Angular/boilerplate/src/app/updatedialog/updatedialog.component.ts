import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-updatedialog',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './updatedialog.component.html',
  styleUrl: './updatedialog.component.scss'
})
export class UpdatedialogComponent {
  constructor(
    public dialogRef: MatDialogRef<UpdatedialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  // Methode, die beim Schließen des Dialogs aufgerufen wird, um die bearbeiteten Daten zurückzugeben
  onSave(): void {
    this.dialogRef.close(this.data);
  }

  // Methode, um den Dialog abzubrechen
  onCancel(): void {
    this.dialogRef.close();
  }
}
