import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {AuthService} from "../AuthService";
import {CustomerLoginDto} from "../dto/customer-Login-Dto";

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  @Output() closeEvent = new EventEmitter<void>();
  @Output() signupEvent = new EventEmitter<void>();


  close(): void {
    this.closeEvent.emit();
  }




  constructor(private http: HttpClient,
              private router: Router, private authService: AuthService,) { }

  ngOnInit(): void {
  }

  errorMessage: string = '';


}
