import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import {CustomerLoginDto} from "../dto/customer-Login-Dto";
import { FormsModule } from '@angular/forms';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import {HttpClient} from "@angular/common/http";
import {Router, Routes} from "@angular/router";
import { AuthService } from '../AuthService';
import {StatusType} from "../../model/StatusType";
import {PurchaseDto} from "../../model/dto/purchase-dto";
import {CustomerDto} from "../../model/dto/customer-dto";
import {CartDto} from "../../model/dto/cart-dto.model";
import {CartItemDto} from "../../model/dto/cart-item-dto";
import {PurchaseDataService} from "../../services/purchase-data.service";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  @Output() closeEvent = new EventEmitter<void>();
  @Output() signupEvent = new EventEmitter<void>();
  @Output() twoFaEvent = new EventEmitter<void>();
  @Output() twoFaNotSetDApp = new EventEmitter<void>();

  openSignup(): void {
    this.signupEvent.emit();
  }
  open2Fa(): void {
    this.twoFaEvent.emit();
  }
  openTwoFaNotSetDApp(){
    this.twoFaNotSetDApp.emit();
  }

  close(): void {
    this.closeEvent.emit();
  }

  //Reset password
  featureNotReady(event: Event) {
    event.preventDefault(); // stops the page from refreshing or navigating
    alert("Feature under construction...");
  }

  constructor(private http: HttpClient, private router: Router, private authService: AuthService, private purchaseDataService: PurchaseDataService) { }

  //When login in, trigger login() and store customer and values
  customerDto: CustomerDto = new CustomerDto(0, "", "", "", "", "", "", "");
  cartDto: CartDto = new CartDto(0, 0, 0, StatusType.pending, this.customerDto);
  cartItems: CartItemDto[] = [];

  purchaseServiceDto: PurchaseDto = new PurchaseDto(
    this.customerDto,
    this.cartDto,
    this.cartItems
  );

  isAuthenticated = false

  ngOnInit(): void {
    //Subscribe to Observable
    this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isAuthenticated = isLoggedIn;
    });
  }

  loginUrl = environment.apiUrl + '/login';
  email: string = '';
  password: string = '';
  errorMessage: string = '';

  //Toast meagerness set to false
  showToast = false;
  //Disable button after submitting
  isSubmitting = false;
  //Processing message set to false
  showProcessing = false;


  onSubmit() {
    console.log('You just clicked Login and are waiting for a server response');
    //Set processing to true
    this.showProcessing = true;
    const sanitizedEmail = this.email.trim().toLowerCase();
    const sanitizedPassword = this.password.trim();
    const loginDto = new CustomerLoginDto(sanitizedEmail, sanitizedPassword);
    // Create customerDTO instance and parse JSON values to it
    this.customerDto = new CustomerDto(0, "", "", sanitizedEmail, "", "", "", "");

    // Update the shared PurchaseDto with this customer
    const current = this.purchaseDataService.getCurrent();
    current.setCustomer(this.customerDto);
    this.purchaseDataService.setData(current);

    //Call login in method
    this.authService.login(sanitizedEmail, sanitizedPassword).subscribe({
      next: (response: any) => {
        console.log('Raw response from server:', response);
        if (response.body.status === "PENDING_2FA") {
            //Open 2FA window
            console.log('Opening 2FA window...');
            this.showProcessing = false;
            this.showToast = true;
            this.isSubmitting = true;
            setTimeout(() => {
              this.open2Fa();
            }, 2000);

        }
        else if(response.body.status === "2FA_NOT_SET"){
            //Open Setup 2FA Authentication. Download Authentication ECommerce APP
            console.log('2FA not set. Download authentication app...');
            //Create customerDTO instance and parse JSON values to it
            const customer = Object.assign(
              new CustomerDto(0, "", "", "", "", "", "", ""),
              response.body.customer
            );
            console.log('Logging in...');
            console.log('Response code:', response.body.status);


            if (customer) {
              console.log('Welcome', customer?.firstName);
              //Set customer
              this.purchaseServiceDto.setCustomer(customer);
              console.log('Successfully customer returned...');

              //Set cart
              this.purchaseServiceDto.getCart().setCustomer(customer);

              //Push updated DTO into the shared service
              this.purchaseDataService.setData(this.purchaseServiceDto);

              console.log("Customer stored in PurchaseDto");

              // set cart status
              this.purchaseServiceDto.getCart().setStatusType(StatusType.pending);


              this.showToast = true;
              this.isSubmitting = true;
              setTimeout(() => {
                console.log("3 seconds are up!");
                //Set user logged to true
                this.authService.setLoggedIn(true);
                //Store firstName
                this.authService.firstName = customer?.firstName;
                //Call method to render authenticated user data
                //this.authService.authenticatedUserMode();
                this.showToast = false;
                this.router.navigate(['/vacation']);
                this.close();
              }, 3000);
              //Open TwoFaNotSetDApp Screen
              this.openTwoFaNotSetDApp();

            }else if (response.body.status === "UNAUTHORIZED") {
              console.log('Error: No customer returned or saved...');
              //Error message if authentication fails.
              this.errorMessage = "Invalid username or password";
              setTimeout(() => {
                this.errorMessage = "";
              }, 3000);
            }


          //},




        }
        else{
            console.log('Error: No customer returned or saved...');
            //Error message if authentication fails.
            this.errorMessage = "Unable to Login. Invalid username or password";
        }
      },
      error: (error) => {
          if (error.status === 401) {
            console.log("Unauthorized: Login failed.");
            //Error message if authentication fails.
            this.errorMessage = "Invalid username or password";
            setTimeout(() => {
              this.showProcessing = false;
              this.errorMessage = "";
            }, 3000);
          }
      }


    });
  }

}
