import { Component, OnInit } from '@angular/core';
import {MapsService} from '../maps.service';
declare let EventSource:any;

@Component({
  selector: 'app-sse-controller',
  templateUrl: './sse-controller.component.html',
  styleUrls: ['./sse-controller.component.scss']
})
export class SseControllerComponent implements OnInit {
 sseData;
  constructor(private mapService: MapsService) { }

  ngOnInit() {

          let source = new EventSource('./api/v1/request');
          source.addEventListener('message', message => {
//               let n: Notification; //need to have this Notification model class in angular2
//               n = JSON.parse(message.data);
              console.log(message.data);
          });

          this.mapService.getSseEmitter().subscribe(data => {
            this.sseData = data;
            console.log(data);
            console.log(this.sseData);
          });


  }

}
