
import {Component, EventEmitter, OnInit, Output} from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { Router, ActivatedRoute, ParamMap } from '@angular/router';

import { map, Observable } from 'rxjs';

import { Vacation } from '../../model/vacation';
import {PurchaseDataService} from "../../services/purchase-data.service";
import {VacationDto} from "../../model/dto/vacation-dto";
import {CartItemDto} from "../../model/dto/cart-item-dto";
import {PurchaseDto} from "../../model/dto/purchase-dto";
import {CustomerDto} from "../../model/dto/customer-dto";
import {CartDto} from "../../model/dto/cart-dto.model";
import {AuthService} from "../../upgrades/AuthService";
import {environment} from "../../../environments/environment";

@Component({
  selector: 'app-vacation-detail',
  templateUrl: './vacation-detail.component.html',
  styleUrls: ['./vacation-detail.component.css']
})
export class VacationDetailComponent implements OnInit {

  @Output() loginEvent = new EventEmitter<void>();


  openLogin(): void {
    this.loginEvent.emit();
  }

  constructor(private http: HttpClient, private route: ActivatedRoute,
              private purchaseDataService: PurchaseDataService, public authService: AuthService) { }


  vacationUrl = environment.apiUrl + '/vacations';

  vacation: Vacation = new Vacation("", "", 0, "", new Date(), new Date(), { self: { href: "" }});
  vacationId: number = 0;

  purchaseServiceDto: any;

  //Check if user is authenticated
  isAuthenticated = false;


  private addCartItem() {
    let tempVacationDto = new VacationDto(0, "", "", 0, "");
    tempVacationDto.setId(this.vacationId);
    tempVacationDto.setVacationTitle(this.vacation.vacation_title);
    tempVacationDto.setTravelPrice(this.vacation.travel_price);

    let tempCartItemDto = new CartItemDto(tempVacationDto, []);
    tempCartItemDto.setVacation(tempVacationDto);

    this.purchaseServiceDto.addCartItem(tempCartItemDto);
  }

  ngOnInit(): void {

    this.authService.isLoggedIn$.subscribe(isLoggedIn => {

      if(!isLoggedIn){
        //Make user sign in
        this.authService.triggerLoginPopup();
      }

    });

    this.purchaseDataService.purchaseServiceData.subscribe(serviceData => {
      this.purchaseServiceDto = serviceData;
    });

    this.vacationId = +this.route.snapshot.paramMap.get('vacationId')!;
    this.getVacation(this.vacationId).subscribe(vacation => {
      this.vacation = vacation;

      this.addCartItem();

      const cartItem = this.purchaseServiceDto.getCurrentCartItem().getVacation();
      cartItem.setId(this.vacation.id);
      cartItem.setVacationTitle(this.vacation.vacation_title);
      cartItem.setDescription(this.vacation.description);
      cartItem.setTravelPrice(this.vacation.travel_price);
      cartItem.setImageUrl(this.vacation.image_URL);

      this.purchaseDataService.setData(this.purchaseServiceDto);
    });
  }


  getVacation(vacationId: number): Observable<Vacation> {
    return this.http.get<Vacation>(`${this.vacationUrl}/${vacationId}`)
        .pipe(
          map(vacation => {
            let parsedId = vacation._links.self.href.split("/")[5];
            vacation.id = parseInt(parsedId);

            return vacation;
          })
        )
  }

  ngOnDestroy(){


  }

}
