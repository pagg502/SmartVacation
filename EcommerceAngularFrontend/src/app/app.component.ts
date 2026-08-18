import {ChangeDetectorRef, Component, ViewChild} from '@angular/core';
import { BreakpointObserver } from '@angular/cdk/layout';
import { MatSidenav } from '@angular/material/sidenav';
import { AuthService } from "src/app/upgrades/AuthService"
import {Router} from "@angular/router";
import {CustomerDto} from "./model/dto/customer-dto";
import {PurchaseDto} from "./model/dto/purchase-dto";
import {PurchaseDataService} from "./services/purchase-data.service";
import {CartDto} from "./model/dto/cart-dto.model";
import {StatusType} from "./model/StatusType";
import {CartItemDto} from "./model/dto/cart-item-dto";
import {VacationDto} from "./model/dto/vacation-dto";
import * as http from "node:http";
import {HttpClient} from "@angular/common/http";
import {MatDateRangeInput} from "@angular/material/datepicker";
import {timeout} from "rxjs";



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  constructor(private observer: BreakpointObserver, public authService: AuthService, private router: Router, private purchaseDataService: PurchaseDataService, private http: HttpClient) { }

  //Will be used to change user UI based on subscription result
  isAuthenticated = false;

  //Hide Elements when needed
  //hideElements = true;

  title = 'Vacation Dashboard';

  showCart = true;
  firstName: string = "";

  @ViewChild(MatSidenav)
  sidenav!: MatSidenav;

  // Added dropdown properties
  destinations: string[] = ['Italy', 'Greece', 'France', 'Belgium', 'Brazil', 'South Dakota', 'Nashville', 'Wisconsin'];
  selectedDestination: string = '';
  @ViewChild('rangeInput') rangeInput!: MatDateRangeInput<Date>;

  //Set pop-up windows to false
  showLogin = false;
  showSignup = false;
  showLogout = false;
  showManageUsers = false;
  showSearch= false;
  showCustomers = false;
  showEditCustomer = false;
  show2Fa = false;
  show2FaNotSetDApp = false;
  showAiChat = false;
  showAskMeButton = true;

  //Switches
  switchToSignup() {
    this.showLogin = false;
    this.showSignup = true;
  }

  switchToLogin() {
    this.showSignup = false;
    this.showLogin = true;
  }

  switchTo2Fa(){
    this.showLogin = false;
    this.show2Fa = true;
    console.log("Switching from login to 2FA!");
  }

  switchToShow2FaNotSetDApp(){
    this.showLogin = false;
    this.show2FaNotSetDApp = true;
  }

  //Close windows
  closeAiChat() {
    this.showAiChat = false;
    this.showAskMeButton = true;
    //##################################################### Call webscoket and terminate connection
  }

  closeLogin() {
    this.showLogin = false;
    if (!this.isAuthenticated) {
      console.log('Redirecting user to main page due to closing login box.');
      this.router.navigate(['/vacation']);
    }
  }

  closeSignup() {
    this.showSignup = false;
  }

  closeLogout() {
    this.showLogout = false;
  }

  closeManageUsers() {
    this.showManageUsers = false;
  }

  closeCustomers(){
    this.showCustomers = false;
  }

  closeSearch() {
    this.showSearch = false;
  }

  closeEditCustomer(){
    this.showEditCustomer = false;
    this.refreshCustomers();
  }

  close2Fa(){
    this.show2Fa = false;
  }

  closeShow2FaNotSetDApp(){
    this.show2FaNotSetDApp = false;
  }

  //Open windows
  open2Fa() {
    this.showLogin = false;
    this.show2Fa = true;
  }

  openAiChat() {
    this.showAiChat = true;
    this.showAskMeButton = false;
   }

  openSignup() {
    this.showLogin = false;
    this.showSignup = true;
  }

  openLogin() {
    this.showSignup = false;
    this.showLogin = true;
  }

  openManageUsers() {
    this.showManageUsers = true;
  }
  openSearch(){
    this.showManageUsers = false;
    this.showSearch = true;
  }
  openCustomers() {
    this.showCustomers = true;
    this.showManageUsers = false;
  }

  selectedCustomerId: number | null = null;

  openEditCustomerM(id: number) {
    this.selectedCustomerId = id;
    this.showEditCustomer = true;
  }

  logOutErrorMessage() {
    alert("Something went wrong");
  }

  openLogout() {
    this.showLogout = true;
    console.log('You just clicked Logout and are waiting for a server response');
    this.authService.logout().subscribe({
      next: (response: any) => {
        console.log("Response from server: ", response["Response"]);

        //Set user logged to false
        this.authService.setLoggedIn(false);
        //Empty firstName
        this.authService.firstName = '';
        setTimeout(() => {
          this.closeLogout();
          this.router.navigate(['/vacation']);
        }, 2000);
      },
      error: (err) => {
        console.error('Logout failed:', err);
        this.closeLogout();
        //Error message if logout fails.
        this.logOutErrorMessage();
      }
    });
  }

  //Refresh page
  refreshCustomers(){
    this.closeCustomers();
    setTimeout(() => {
      this.openCustomers();

    } , 10);
  }

  //Allow others to open login and signup through events:
  onRouteActivate(component: any) {
    if (component.loginEvent) {
      component.loginEvent.subscribe(() => this.openLogin());
    }
  }


  //Check user session
  checkUserSession(){

    this.authService.checkSession().subscribe({
      next: (response: any) => {

        //Create customerDTO instance and parse JSON values to it
        const customer = Object.assign(
          new CustomerDto(0, "", "", "", "", "", "", ""),
          response.body
        );
        if (customer) {
          //Set customer
          this.purchaseServiceDto.setCustomer(customer);
          console.log('Successfully customer returned...');

          //Set cart
          this.purchaseServiceDto.getCart().setCustomer(customer);

          //Push updated DTO into the shared service
          this.purchaseDataService.setData(this.purchaseServiceDto);

          console.log("Customer stored in PurchaseDto");
        }else {
          console.log('Error: No customer returned or saved...');
        }

        console.log("User is logged in:", customer.firstName);
        this.authService.setLoggedIn(true);
        //Set user firstname
        this.authService.firstName = customer?.getFirstName();
        //Call method to render authenticated user data
        //this.authService.setLoggedIn(true);
        //Close login popup-window
        this.showLogin = false;

      },
      error: (email: string) => {
        console.log("No active session. Email: ", email);
        this.authService.setLoggedIn(false);
      }
    });

  }


  //When refreshing the app, trigger check session and store customer and values to use them later during Checkout
  customerDto: CustomerDto = new CustomerDto(0, "", "", "", "", "", "", "");
  cartDto: CartDto = new CartDto(0, 0, 0, StatusType.pending, this.customerDto);
  cartItems: CartItemDto[] = [];


  purchaseServiceDto: PurchaseDto = new PurchaseDto(
    this.customerDto,
    this.cartDto,
    this.cartItems
  );


  ngOnInit(): void {
    //Open AI Chat window
    setTimeout(() => {
              this.showAiChat = true;
              this.showAskMeButton = false;
            }, 2000);

    //Subscribe to Observable to change user layout
      this.authService.isLoggedIn$.subscribe(isLoggedIn => {
        this.isAuthenticated = isLoggedIn;
      });

    //Subscribe to BehaviorSubject to popup login window when needed
    this.authService.loginPopup$.subscribe(show => {
      if (show) {
        this.openLogin();
      }
    });


    const customer = this.authService.currentCustomer;
    this.firstName = customer?.firstName ?? '';

    this.checkUserSession()

  }

  ngAfterViewInit() {
    this.observer.observe(['(max-width: 800px)']).subscribe((res) => {

      if (this.sidenav) {
        // responsive sidenav logic
        if (res.matches) {
          this.sidenav.mode = 'over';
          this.sidenav.close();
        } else {
          this.sidenav.mode = 'side';
          this.sidenav.open();
        }
      }


    });
  }
  //Scroll down to destinations
  scrollToSection() {
    const element = document.getElementById('target-section');
    if (element) {
      element.scrollIntoView({ behavior: 'smooth' });
      this.selectedDestination = ''
    }
  }

  //ManageUsers window
  protected manageUsers() {

    this.openManageUsers()

  }

}
