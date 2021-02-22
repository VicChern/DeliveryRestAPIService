import { Component, OnInit } from '@angular/core';
import {MapsService} from '../maps.service';
import {TrackingLocation} from './Location';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.scss']
})
export class MapComponent implements OnInit {
  // Angular 7 Google Maps Tutorial
  // https://www.youtube.com/watch?v=-IwTQgKIjCQ

  trackingLocation: Location = new Location();

  constructor(private mapService: MapsService) {
  }

  ngOnInit() {

    // Display Current Location Using Google Map
    // https://www.codementor.io/@brijmcq/angular-display-current-location-using-google-map-fnl3tosdq
    if (navigator) {
      navigator.geolocation.getCurrentPosition(pos => {
        console.log('Geolocation Position: ', pos);
        this.trackingLocation.lat = pos.coords.latitude;
        this.trackingLocation.lon = pos.coords.longitude;
      });
    }


    // if we use ipapi service (https://ipapi.co/)
//     this.mapService.getLocation().subscribe(data => {
//       console.log(data);
//       this.lat = data.latitude;
//       this.lng = data.longitude;
//     });
  }
}

export class Location {
  lat: number;
  lon: number;
}
