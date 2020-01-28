import { Component, OnInit } from '@angular/core';
import {MapsService} from '../maps.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {
 testCodeFromBack: string;
  constructor(private mapService: MapsService) { }

  ngOnInit() {
  this.mapService.getSomeTestCodeFromBack().subscribe(data => {
        console.log(data);
        this.testCodeFromBack = data;
      });

  }

}
