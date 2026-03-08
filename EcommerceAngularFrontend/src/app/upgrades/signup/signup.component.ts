import {Component, OnInit, EventEmitter, Output, NgModule} from '@angular/core';
import {Country} from "../../model/country";
import {Division} from "../../model/division";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {CountryApiResponse} from "../../model/country-api-response";
import {DivisionApiResponse} from "../../model/division-api-response";
import {AuthService} from "../AuthService";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {

  @Output() closeEvent = new EventEmitter<void>();
  @Output() loginEvent = new EventEmitter<void>();

  openLogin(): void {
    this.loginEvent.emit();
  }

  close(): void {
    this.closeEvent.emit();
  }

  signup:string = "Signup";
  addCustomer:string = "Add Customer";
  isAuthenticated = false;


  registerCustomerUrl = environment.apiUrl + '/register';
  countryUrl = environment.apiUrl + '/countries';
  divisionUrl = environment.apiUrl + '/divisions';

  countries: Country[] = [];
  divisions: Division[] = [];

  firstName: string = '';
  lastName: string = '';
  email: string = '';
  password: string = '';
  address: string = '';
  postal_code: string = '';
  phone: string = '';
  countryChoice: string = '';

  //divisionChoice: Division = new Division(0, "", 0, {country: {href: ""}, self: {href: ""}});
  divisionChoice: number | null = null;
  pwdConf: string = '';

  constructor(private http: HttpClient,
              private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    //Subscribe to Observable to change user layout
    this.authService.isLoggedIn$.subscribe(isLoggedIn => {
      this.isAuthenticated = isLoggedIn;
    });

    this.getCountries().subscribe(countries => this.countries = countries);
    this.getDivisions().subscribe(divisions => this.divisions = divisions);
  }

  getCountries(): Observable<Country[]> {
    return this.http.get<CountryApiResponse>(this.countryUrl)
      .pipe(
        map(response => response._embedded.countries)
      )
  }

  getDivisions(): Observable<Division[]> {
    return this.http.get<DivisionApiResponse>(this.divisionUrl)
      .pipe(
        map(response => response._embedded.divisions)
      )
  }

  getDivisionsByCountryId(id: any): Division[] {
    return this.divisions.filter(division => {
      return division.country_id == parseInt(id);
    });
  }

  //Toast meagerness set to false
  showToast = false;
  //Disable button after submitting
  isSubmitting = false;
  displayMessage: string = '';


  onSubmit() {
    let customer = {
      firstName: this.firstName,
      lastName: this.lastName,
      email: this.email,
      password: this.password,
      address: this.address,
      postal_code: this.postal_code,
      phone: this.phone,
      country: this.countryChoice,
      division: this.divisionChoice
    }

    // post to customer
    this.http.post(this.registerCustomerUrl, customer).subscribe({
      next: (response: any) => {
        this.showToast = true;
        this.isSubmitting = true;
        this.displayMessage = "";
        setTimeout(() => {
          this.showToast = false;
          this.close();
        }, 3000);
      },
      error: err => {
        console.error("Registration failed", err);
        if (err.status === 409) {
          this.displayMessage = 'Email address already exists!';
        }

      }
    });
  }

}
