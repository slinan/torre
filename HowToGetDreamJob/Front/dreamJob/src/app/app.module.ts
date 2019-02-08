import {
  BrowserModule
} from '@angular/platform-browser';
import {
  NgModule
} from '@angular/core';
import {
  AppRoutingModule
} from './app-routing.module';
import {
  AppComponent
} from './app.component';
import {
  MainViewComponent
} from './main-view/main-view.component';
import {
  BrowserAnimationsModule
} from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatInputModule,
  MatCheckboxModule,
  MatToolbarModule,
  MatTabsModule,
  MatCardModule,
  MatStepperModule,
  MatFormFieldModule,
  MatGridListModule,
  MatSelectModule
} from '@angular/material';
import {
  Component2Component
} from './component2/component2.component';
import {
  FormsModule,
  ReactiveFormsModule
} from '@angular/forms';

@NgModule({
  declarations: [
    AppComponent,
    MainViewComponent,
    Component2Component,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatToolbarModule,
    MatTabsModule,
    MatCardModule,
    MatStepperModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatGridListModule,
    MatSelectModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
