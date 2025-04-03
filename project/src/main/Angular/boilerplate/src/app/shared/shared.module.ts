import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormsModule} from '@angular/forms';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {provideNativeDateAdapter} from '@angular/material/core';
import {MatTableModule} from '@angular/material/table';
import {MAT_DIALOG_DATA,MatDialog,MatDialogActions,MatDialogClose,MatDialogContent,MatDialogRef,MatDialogTitle,} from '@angular/material/dialog';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatDividerModule} from '@angular/material/divider';
import {FormControl, ReactiveFormsModule} from '@angular/forms';
import {MatSelectModule} from '@angular/material/select';






@NgModule({
  declarations: [
  ],
  imports: [
    CommonModule,
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatGridListModule,
    FormsModule, 
    MatFormFieldModule, 
    MatInputModule,
    MatDatepickerModule,
    MatTableModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatCheckboxModule,
    MatDividerModule,
    MatSelectModule,
    ReactiveFormsModule
  ],
  exports: [
    MatToolbarModule,
    MatIconModule,
    MatButtonModule,
    MatGridListModule,
    FormsModule, 
    MatFormFieldModule, 
    MatInputModule,
    MatDatepickerModule,
    MatTableModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatCheckboxModule,
    MatDividerModule,
    MatSelectModule,
    ReactiveFormsModule
  ],
  providers: [provideNativeDateAdapter()],
})
export class SharedModule { }
