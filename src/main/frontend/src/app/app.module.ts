import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { AdminComponent } from './admin/admin.component';
import { CustomerAccountComponent } from './customer-account/customer-account.component';
import { RegistrationComponent } from './registration/registration.component';
import {FormsModule} from '@angular/forms';
import { MapComponent } from './map/map.component';
import {AgmCoreModule} from '@agm/core';
import {HttpClientModule} from '@angular/common/http';
import {MapsService} from './maps.service';
import { SseControllerComponent } from './sse-controller/sse-controller.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    AdminComponent,
    CustomerAccountComponent,
    RegistrationComponent,
    MapComponent,
    SseControllerComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyCp1l20e2pXx3bcI-kshZvzjzlrkOhem0Y'
    })
  ],

  providers: [MapsService],
  bootstrap: [AppComponent]
})
export class AppModule { }
