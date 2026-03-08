import {Injectable} from "@angular/core";
import {CanActivate} from "@angular/router";
import {AuthService} from "./AuthService";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

  constructor(private auth: AuthService) {}

  canActivate(): boolean {
    let loggedIn = false;

    this.auth.isLoggedIn$.subscribe(v => loggedIn = v).unsubscribe();

    if (!loggedIn) {
      this.auth.triggerLoginPopup();
      return false;
    }

    return true;
  }
}
