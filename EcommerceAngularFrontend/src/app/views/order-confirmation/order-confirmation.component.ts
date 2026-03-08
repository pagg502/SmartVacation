import {HttpClient} from '@angular/common/http';
import {Component, destroyPlatform, EventEmitter, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {CartItem} from 'src/app/model/cart-item';
import {Customer} from 'src/app/model/customer';
import {PurchaseApiResponse} from 'src/app/model/purchase-api-response';
import {Vacation} from 'src/app/model/vacation';
import {PurchaseDataService} from "../../services/purchase-data.service";
import {CustomerDto} from "../../model/dto/customer-dto";
import {CartDto} from "../../model/dto/cart-dto.model";
import {PurchaseDto} from "../../model/dto/purchase-dto";
import {StatusType} from "../../model/StatusType";
import {VacationDto} from "../../model/dto/vacation-dto";
import {ExcursionDto} from "../../model/dto/excursion-dto";
import {CartItemDto} from "../../model/dto/cart-item-dto";
import {environment} from "../../../environments/environment";


@Component({
  selector: 'app-order-confirmation',
  templateUrl: './order-confirmation.component.html',
  styleUrls: ['./order-confirmation.component.css']
})
export class OrderConfirmationComponent implements OnInit {

  //Tell app.component to check user session after redirection
  @Output() checkUserSession = new EventEmitter<void>();
  @Output() closeWindow = new EventEmitter<void>();

  cartItemsUrl = "";
  checkoutUrl = environment.apiUrl + "/checkout/purchase";
  customerUrl = environment.apiUrl + "/customers/1";
  cartsUrl = environment.apiUrl + "/carts";
  cartId = 0;

  cartItems: CartItem[] = [];
  vacations: Set<Vacation> = new Set();
  customer: Customer = new Customer(0, "", "", "", "", "", "", "", 0)

  orderTrackingNumber: string = ""

  purchaseServiceDto: any;

  customerDto: CustomerDto = new CustomerDto(0, "", "", "", "", "", "","");

  constructor(private http: HttpClient,
              private route: ActivatedRoute,
              private purchaseDataService: PurchaseDataService,
              private router: Router
  ) {
  }

  ngOnInit(): void {
    // new purchase data service dto
    this.purchaseDataService.purchaseServiceData.subscribe((serviceData) => {
      // console.log('Excursion Detail Current Data Service:', serviceData);
      this.purchaseServiceDto = serviceData;
    })

    // get customer dto from data service
    this.customerDto = this.purchaseServiceDto.getCustomer();

    this.checkout();

  }

  ngOnDestroy() {
    // reset the data service
    this.purchaseDataService.clearCart();

  }

  checkout() {

    // convert data service object to plain old object for api call
    let purchasePlain = Object.assign({}, this.purchaseServiceDto);

    // send request to back end
    this.http.post<PurchaseApiResponse>(this.checkoutUrl, purchasePlain).subscribe(response => {
      console.log("response including order tracking number from server: ", response);
      this.orderTrackingNumber = response.orderTrackingNumber;
    });

  }

  close() {
    //this.checkUserSession.emit();
    this.router.navigate(['/vacation']);
    //this.closeWindow.emit();
  }
}
