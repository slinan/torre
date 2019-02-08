import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {MainViewComponent} from './main-view/main-view.component';
import {Component2Component} from './component2/component2.component';

const routes: Routes = [
  { path: '', redirectTo: '/first', pathMatch: 'full' },
  { path: 'first', component:  MainViewComponent},
  { path: 'second', component:  Component2Component},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
