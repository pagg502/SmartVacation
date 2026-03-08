import { Component, OnInit, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-two-fa-not-set-download-app',
  templateUrl: './two-fa-not-set-download-app.component.html',
  styleUrls: ['./two-fa-not-set-download-app.component.css']
})
export class TwoFaNotSetDownloadAppComponent implements OnInit {
  @Output() closeEvent = new EventEmitter<void>();
  constructor() { }
  close(){
    this.closeEvent.emit();
  }
  ngOnInit(): void {
  }

}
