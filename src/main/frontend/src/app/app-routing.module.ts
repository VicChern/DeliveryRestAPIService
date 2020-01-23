import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {AdminComponent} from './admin/admin.component';
import {CustomerAccountComponent} from './customer-account/customer-account.component';
import {RegistrationComponent} from './registration/registration.component';
import {MapComponent} from './map/map.component';


const routes: Routes = [
  {
    path: 'adminPage',
    component: AdminComponent,
  },
  {
    path: 'customer-account',
    component: CustomerAccountComponent,
  },
  {
    path: 'registration',
    component: RegistrationComponent,
  },
  {
    path: 'map',
    component: MapComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
