import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { AuthService } from '../AuthService';
import { StatusType } from "../../model/StatusType";
import { CustomerDto } from "../../model/dto/customer-dto";
import { CartDto } from "../../model/dto/cart-dto.model";
import { CartItemDto } from "../../model/dto/cart-item-dto";
import { PurchaseDto } from "../../model/dto/purchase-dto";
import { PurchaseDataService } from "../../services/purchase-data.service";
import { Router } from "@angular/router";
import { WebSocketService } from '../WebSocketService';
import { environment } from '../../../environments/environment';

@Component({
  selector: 'app-two-factor-authentication',
  templateUrl: './two-factor-authentication.component.html',
  styleUrls: ['./two-factor-authentication.component.css']
})
export class TwoFactorAuthenticationComponent implements OnInit {

  @Output() closeEvent = new EventEmitter<void>();

  showToast = false;
  errorMessage = false;
  errorTimedOut = false
  //Create variable to hold the timer reference
  private timeoutId: any;


  customerDto!: CustomerDto;
  purchaseDto!: PurchaseDto;

  constructor(
    private webSocket: WebSocketService,
    private authService: AuthService,
    private purchaseDataService: PurchaseDataService,
    private router: Router
  ) {}

  close(): void {
    this.closeEvent.emit();
  }

  ngOnInit(): void {
    // Load existing PurchaseDto and customer
    this.purchaseDto = this.purchaseDataService.getCurrent();
    this.customerDto = this.purchaseDto.getCustomer();

//     // Create variable to hold the timer reference
//     let timeoutId;

    //Start the timer before connection call
    this.timeoutId = setTimeout(() => {
      console.error("Connection timed out after 15 seconds.");

      //CLose webSocket connection if we don't receive a response within 15s
      if (this.webSocket) {
        this.webSocket.disconnect();
      }

      //Display timed out
      this.errorTimedOut = true
    }, 15000);

    // Connect WebSocket using the real email
    this.webSocket.connect(
      this.customerDto.getEmail(),
      payload => this.handle2FAUpdate(payload)
    );
  }

  handle2FAUpdate(payload: any) {
    if (payload.status === "APPROVED") {
      //Disconnect WebSocket
      this.webSocket.disconnect();
      //Cancel timer
      clearTimeout(this.timeoutId);
      const customer = Object.assign(
        new CustomerDto(0, "", "", "", "", "", "", ""),
        payload.customer
      );

      // Update DTOs
      this.purchaseDto.setCustomer(customer);
      this.purchaseDto.getCart().setCustomer(customer);
      this.purchaseDto.getCart().setStatusType(StatusType.pending);

      // Push updated DTO into shared service
      this.purchaseDataService.setData(this.purchaseDto);

      // Auth success
      this.authService.setLoggedIn(true);
      this.authService.firstName = customer.firstName;

      this.showToast = true;

      setTimeout(() => {
        this.showToast = false;
        this.router.navigate(['/vacation']);
        this.close();
      }, 3000);

    } else {
      clearTimeout(this.timeoutId);
      this.errorMessage = true;
    }
  }
}
