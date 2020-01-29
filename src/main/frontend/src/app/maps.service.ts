import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TrackingLocation} from './map/Location';

@Injectable({
  providedIn: 'root'
})
export class MapsService {

  constructor(private http: HttpClient) { }

// if we use ipapi service (https://ipapi.co/)
//   getLocation() {
//     return this.http.get<Location>('http://api/ipapi.com/api/check?access_key=');
//   }

  getSomeTestCodeFromBack() {
    return this.http.get<any>('./api/v1/users/83');
  }

}

