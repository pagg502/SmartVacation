import { HttpClient } from '@angular/common/http';

import {Component, OnInit, ChangeDetectorRef, Output, EventEmitter, Input} from '@angular/core';
import { FormsModule, FormBuilder, FormControl, FormGroup, NgSelectOption, NgModel } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, map, filter } from 'rxjs';

import { Country } from 'src/app/model/country';
import { CountryApiResponse } from 'src/app/model/country-api-response';
import { Customer } from 'src/app/model/customer';
import { Division } from 'src/app/model/division';
import { DivisionApiResponse } from 'src/app/model/division-api-response';
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-edit-customer',
  templateUrl: './edit-customer.component.html',
  styleUrls: ['./edit-customer.component.css']
})

export class EditCustomerComponent implements OnInit {

  //@Output() openEditCustomer = new EventEmitter<void>();
  @Output() closeEvent = new EventEmitter<void>();


  closeEventM(): void {
    this.closeEvent.emit();
  }

  @Input() customerId: number | null = null;


  customerUrl = environment.apiUrl + '/customers';
  countryUrl = environment.apiUrl + '/countries';
  divisionUrl = environment.apiUrl + '/divisions';

  countries: Country[] = [];
  divisions: Division[] = [];
  filteredDivisions: Division[] = [];

  customer: Customer = new Customer(0, '', '','','', "", "",'', 0, {})
  countryChoice: Country = new Country(0, '', { self: { href: ''} });
  divisionChoice: Division = new Division(0, '', 0, { country: { href: ''}, self: { href: '' }});

  constructor(
    private http: HttpClient,
    private router: Router,
    private form: FormBuilder,
    private changeDetect: ChangeDetectorRef
    ) { }

  ngOnInit(): void {

    // Parse customer Id
    //this.customer.id = parseInt(this.router.url.split("/")[2])
    if (this.customerId !== null) {
      this.customer.id = this.customerId;


    let url = this.customerUrl + "/" + this.customer.id

    // Load dropdowns
    this.getCountries().subscribe((countries) => {
      this.countries = countries;

      this.getDivisions().subscribe((divisions) => {
        this.divisions = divisions;

        // Load customer
        this.http.get<Customer>(url).subscribe((customer) => {
          this.customer = customer

          // Load division
          url += "/division";
          this.http.get<Division>(url).subscribe((division) => {
            this.customer.division_id = division.id

            // Customer is fully loaded.
            console.table(this.customer)

            // Select current values from dropdowns
            this.filterDivisionsByCountry(division.country_id);
            let country = (this.getCountryById(division.country_id));

            this.countryChoice = country;
            this.divisionChoice = division;

          });
        });
      });
    });
    }else{
      console.log("Customer ID not found. Unable to edit Customer");
    }

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

  getCountryById(id: number): Country {
    return this.countries.find(country => {
      return country.id == id
    }) ?? new Country(0,'',{ self: { href: '' } });
  }

  filterDivisionsByCountry(selectedCountry: any) {
    console.table(selectedCountry);
    this.filteredDivisions = this.divisions.filter(division => {
      return division.country_id == parseInt(selectedCountry);
    });
  }

  onSubmit() {
    let customer = {
      firstName: this.customer.firstName,
      lastName: this.customer.lastName,
      email: this.customer.email,
      password: this.customer.password,
      address: this.customer.address,
      postal_code: this.customer.postal_code,
      phone: this.customer.phone,
      division: this.divisionUrl + "/" + this.divisionChoice.id
    }

    // this.http.put(this.customerUrl + "/" + this.customer.id, customer).subscribe();

    this.http.put(this.customerUrl + "/" + this.customer.id, customer).subscribe(
      response => {
        console.log("Customer updated successfully:", response);
        this.closeEventM();
      },
      error => {
        console.error("Error updating customer:", error);
      }
    );

  }
}
