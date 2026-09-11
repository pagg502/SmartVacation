import {Inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Customer} from "../model/customer";
import * as Console from "node:console";
import {CustomerDto} from "../model/dto/customer-dto";
import {BehaviorSubject} from "rxjs";
import {environment} from "../../environments/environment";

@Injectable({ providedIn: 'root' })
export class AuthService {

  private loggedIn = new BehaviorSubject<boolean>(false);
  isLoggedIn$ = this.loggedIn.asObservable();

  loginPopup$ = new BehaviorSubject<boolean>(false);

  triggerLoginPopup() {
    this.loginPopup$.next(true);
  }



  currentCustomer: Customer | null = null;
  firstName: string | null | undefined = null;

  constructor(private http: HttpClient) {}

  checkSession() {
    return this.http.get<CustomerDto>(environment.apiUrl + '/status', {
      withCredentials: true, observe: 'response'
    });
  }

  login(email: string, password: string) {
    return this.http.post<any>(environment.apiUrl + '/login',
      { email, password },
      { withCredentials: true, observe: 'response' }
    );
  }


  setLoggedIn(value: boolean) {
    this.loggedIn.next(value);
  }

  logout() {
    return this.http.delete<any>(environment.apiUrl + '/logout', {
      withCredentials: true
    });
  }

  //Will be switched on and off depending on user authentication status
  //isAuthenticated = false;
  ////Method to display authenticated user data
  //authenticatedUserMode(): void {
  //  console.log("Redirecting to authenticated: " + this.firstName);
  //  this.isAuthenticated = this.loggedIn;
  //}
}
