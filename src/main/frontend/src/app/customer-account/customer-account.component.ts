import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-customer-account',
  templateUrl: './customer-account.component.html',
  styleUrls: ['./customer-account.component.scss']
})
export class CustomerAccountComponent implements OnInit {
  customerAccount: CustomerAccount = new CustomerAccount();
  constructor() { }

  ngOnInit() {
    this.customerAccount.email = 'someEmail@gmail.com';
    this.customerAccount.name = 'SoftServe';
    this.customerAccount.phone = '+380509999999';
    this.customerAccount.address = 'Kyiv, Leipzigskay 15';
  }

}

export class CustomerAccount {
  name: string;
  email: string;
  phone: string;
  address: string;
}
