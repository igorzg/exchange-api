import {BrowserModule} from "@angular/platform-browser";
import {NgModule} from "@angular/core";
import {AppComponent} from "./app.component";
import {MatButtonModule, MatDatepickerModule, MatFormFieldModule, MatInputModule, MatSelectModule} from "@angular/material";
import {MatMomentDateModule} from "@angular/material-moment-adapter";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    MatMomentDateModule,
    MatDatepickerModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatButtonModule,
    BrowserModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
