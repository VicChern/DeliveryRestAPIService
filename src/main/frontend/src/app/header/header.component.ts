import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  readonly APP_NAME = `KV-061.java`;
  constructor() { }

  ngOnInit() {
  }

}
