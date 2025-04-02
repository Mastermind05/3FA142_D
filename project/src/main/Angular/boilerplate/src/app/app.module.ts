import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SharedModule } from './shared/shared.module';
import { StartseiteModule } from './home/home.module';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import { LoginComponent } from './login/login/login.component';
import { UpdatedialogComponent } from './updatedialog/updatedialog.component';
import { ReadingComponent } from './reading/reading/reading.component';
import { SettingdialogComponent } from './settingdialog/settingdialog.component';
import { CreatereadingdialogComponent } from './createreadingdialog/createreadingdialog.component';
import { UpdateReadingDialogComponent } from './updatereadingdialog/updatereadingdialog.component';



@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ReadingComponent,
    CreatereadingdialogComponent,
    UpdateReadingDialogComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SharedModule,
  ],
  providers: [
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
