import {Component, OnInit} from '@angular/core';

import {HttpClient} from '@angular/common/http';

import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

import {VacationApiResponse} from '../../model/vacation-api-reponse';
import {Vacation} from '../../model/vacation';
import {PurchaseDataService} from "../../services/purchase-data.service";
import {Customer} from "../../model/customer";
import {Cart} from "../../model/cart";
import {CartApiResponse} from "../../model/cart-api-response";
import {CartDto} from "../../model/dto/cart-dto.model";
import {StatusType} from "../../model/StatusType";
import {CustomerDto} from "../../model/dto/customer-dto";
import {CustomerApiResponse} from "../../model/customer-api-response";
import {AuthService} from "../../upgrades/AuthService";
import {environment} from "../../../environments/environment";


/**
 *
 */
@Component({
  selector: 'app-vacation',
  templateUrl: './vacation.component.html',
  styleUrls: ['./vacation.component.css']
})
export class VacationComponent implements OnInit {

  // urls
  vacationUrl = environment.apiUrl + '/vacations';
  cartsUrl = environment.apiUrl + '/carts';
  customerUrl = environment.apiUrl + "/customers";

  // vacations for the page
  vacations: Vacation[] = [];

  // data service payload
  purchaseServiceDto: any;

  // customer
  customer: Customer = new Customer(0, "", "", "", "","", "", "", 0)

  status: StatusType = StatusType.pending;
  customerDto: CustomerDto = new CustomerDto(0, "", "", "", "", "", "", "");
  cartDto: CartDto = new CartDto(0,0, 0, this.status , this.customerDto);

  allCarts: Cart [] = [];
  newCartDto: CartDto = new CartDto(0,0, 0, this.status , this.customerDto);
  customers: Customer[] = [];
  newestCustomerInDB: number = 0;
  lastCartId: number = 0;

  //block cart for unauthenticated users:
  blocked = true;

  constructor(private http: HttpClient,
              private purchaseDataService: PurchaseDataService, private authService: AuthService) { }

  ngOnInit(): void {
    //Subscribe to Observable
    this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      this.blocked = isLoggedIn;
    });

    // purchase data service
    this.purchaseDataService.purchaseServiceData.subscribe((serviceData) => {
      // console.log('Excursion Detail Current Data Service:', serviceData);
      this.purchaseServiceDto = serviceData;
    })


    this.getVacations().subscribe(vacations => {
      vacations.forEach(vacation => {
        let parsedId = vacation._links.self.href.split("/")[5];
        vacation.id = parseInt(parsedId);
      });
      this.vacations = vacations;
    });

  }// end on init

  getVacations(): Observable<Vacation[]> {
    return this.http.get<VacationApiResponse>(this.vacationUrl)
      .pipe(
        map(response => response._embedded.vacations)
      )
  }

}
