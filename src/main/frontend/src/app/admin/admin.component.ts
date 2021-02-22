import { Component, OnInit } from '@angular/core';
import {MapsService} from '../maps.service';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.scss']
})
export class AdminComponent implements OnInit {

  constructor(private mapService: MapsService) { }

  ngOnInit() {

  }

}
