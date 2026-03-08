import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Country} from "../../model/country";
import {Division} from "../../model/division";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {CountryApiResponse} from "../../model/country-api-response";
import {DivisionApiResponse} from "../../model/division-api-response";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  @Output() openCustomers = new EventEmitter<void>();
  @Output() openSearch = new EventEmitter<void>();
  @Output() closeEvent = new EventEmitter<void>();


  openCustomersM(): void {
    this.openCustomers.emit();
  }

  openSearchM(): void {
    this.openSearch.emit();
  }

  close(){
    this.closeEvent.emit();
  }


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
  //countryChoice: Country = new Country(0, "", {self: {href: ""}});
  countryChoice: string = '';

  //divisionChoice: Division = new Division(0, "", 0, {country: {href: ""}, self: {href: ""}});
  divisionChoice: number | null = null;
  pwdConf: string = '';

  constructor(private http: HttpClient,
              private router: Router) {}

  ngOnInit(): void {
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

  showfNReport = false;
  fNReport: any = null;
  firstNameReport(){
    this.clearReports()
    this.displayMessage = '';
    const body = { firstName: this.firstName };
    if(this.firstName == ''){
      this.displayMessage = 'Please enter First Name.';
      return;
    }
      this.http.post<any>(environment.apiUrl + '/searchByFirstName', body, {
        withCredentials: true, observe: 'response'}).subscribe({
        next: (response: any) => {
          console.log(response.body);
          this.fNReport = response.body;
          this.showfNReport = true;
        },
        error: err => {
          console.error("FirstName Report failed", err);
          this.displayMessage = 'FirstName Report failed';
        }
      });
  }

  aCReport: any = null;
  phoneAreaCode: string = '';
  //Area Code report
  areaCodeReport(){
    this.clearReports()
    this.displayMessage = '';
    const body = { phone: this.phoneAreaCode };
    if(this.phoneAreaCode == ''){
      this.displayMessage = 'Please enter Phone Area Code.';
      return
    }
      this.http.post<any>(environment.apiUrl + '/searchByPhoneAreaCode', body, {
        withCredentials: true, observe: 'response'}).subscribe({
        next: (response: any) => {
          console.log(response.body);
          this.aCReport = response.body;
        },
        error: err => {
          console.error("Phone Area Code Report failed", err);
          this.displayMessage = 'Phone Area Code Report failed';
        }
      });
  }



  //Clear all reports
  clearReports(){
    this.fNReport = null;
    this.aCReport = null;
  }


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
