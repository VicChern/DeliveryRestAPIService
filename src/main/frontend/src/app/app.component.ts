import { Component, OnInit } from '@angular/core';
import {MapsService} from './maps.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
    title = 'frontend';
   user:any;
    constructor(private mapService: MapsService) { }

    ngOnInit() {
    this.mapService.getSomeTestCodeFromBack().subscribe(data => {
          console.log(data);
          this.user = data;
     });

    }


}

