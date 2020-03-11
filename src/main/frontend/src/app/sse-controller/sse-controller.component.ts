import { Component, OnInit } from '@angular/core';
import {MapsService} from '../maps.service';
import {TrackingLocation} from "../map/Location";
import {ActivatedRoute} from "@angular/router";
import {API_URL} from "../config";

declare let EventSource:any;

@Component({
  selector: 'app-sse-controller',
  templateUrl: './sse-controller.component.html',
  styleUrls: ['./sse-controller.component.scss']
})
export class SseControllerComponent implements OnInit {
  sseData: TrackingLocation;
  orderGuid: string;
  uri = `${API_URL}`;

  constructor(private mapService: MapsService, private route: ActivatedRoute) { }

  ngOnInit() {

    this.route.paramMap.subscribe(params => {
      this.orderGuid = params.get('orderGuid')
    });

          let source = new EventSource('./api/v1/orders/' + this.orderGuid + '/tracking/');
          console.log("event source: " + source);
          source.addEventListener('message', message => {
//               let n: Notification; //need to have this Notification model class in angular2
              console.log('message: ', message);
              console.log('message.data: ', message.data);
              this.sseData = JSON.parse(message.data);
//               this.sseData.lon = Number(message.lon);
//               this.sseData.lat = Number(message.lat);
              console.log('sseData: ', this.sseData);
          });

//           this.mapService.getSseEmitter().subscribe(data => {
// //             this.sseData = data;
//             console.log('$$$$$$$$$', data);
// //             console.log('5555555555555555', this.sseData);
//           });


  }

}
