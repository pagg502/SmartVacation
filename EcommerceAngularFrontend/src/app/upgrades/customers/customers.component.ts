import {ChangeDetectorRef, Component, EventEmitter, OnInit, Output} from '@angular/core';
import {Customer} from "../../model/customer";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {CustomerApiResponse} from "../../model/customer-api-response";
import {map} from "rxjs/operators";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {

  @Output() closeCustomer = new EventEmitter<void>();
  @Output() addCustomer = new EventEmitter<void>();
  @Output() closeAddCustomer = new EventEmitter<void>();
  @Output() refreshCustomers = new EventEmitter<void>();

  @Output() openEditCustomer = new EventEmitter<number>();

  openEditCustomerM(customerId: number): void {
    this.openEditCustomer.emit(customerId);
  }

  addCustomerM(): void {
    this.addCustomer.emit();
  }

  closeCustomerM(): void {
    this.closeCustomer.emit();
  }

  closeAddCustomerM(){
    this.closeAddCustomer.emit();
  }

  refreshCustomersM(){
    this.refreshCustomers.emit();
  }

  customerUrl = environment.apiUrl + '/customers'

  customers: Customer[] = [];

  constructor(private http: HttpClient,
              private cdr: ChangeDetectorRef) { }

  ngOnInit(): void {
    this.getCustomers().subscribe(customers => this.customers = customers);
  }

  ngAfterViewChecked(): void {
    this.cdr.detectChanges()
  }

  getCustomers(): Observable<Customer[]> {
    return this.http.get<CustomerApiResponse>(this.customerUrl)
      .pipe(
        map(response => response._embedded.customers)
      )
  }

//Delete customer
  deleteCustomerM(customerId: number) {
    this.http.delete(this.customerUrl + "/" + customerId).subscribe({
      next: () => {
        console.log("Customer deleted:", customerId);
        this.refreshCustomersM();
      },
      error: err => console.error("Delete failed:", err)
    });
  }




}
