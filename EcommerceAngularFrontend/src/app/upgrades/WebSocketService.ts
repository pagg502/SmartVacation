import { Injectable } from '@angular/core';
import { Client, IMessage } from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private client!: Client;

  connect(userEmail: string, on2FAUpdate: (payload: any) => void) {
    this.client = new Client({
      webSocketFactory: () => new SockJS(environment.apiUrl + '/websocket'),
      reconnectDelay: 5000
    });

    this.client.onConnect = () => {
      console.log('Connected to WebSocket');

     //Get stored pending_2fa_email
     //const userEmail = sessionStorage.getItem('pending_2fa_email');

     this.client.subscribe(`/messages/2fa/${userEmail}`, (message: IMessage) => {
       const payload = JSON.parse(message.body);
       console.log('2FA update received:', payload);
       on2FAUpdate(payload);
     });
    };

    this.client.activate();
  }

  send2FARequest(fcmToken: string) {
    // Send to Spring's @MessageMapping("/2fa") with application prefix "/backend"
    this.client.publish({
      destination: '/backend/2fa',
      body: fcmToken
    });
  }

  disconnect() {
    if (this.client && this.client.active) {
      this.client.deactivate();
      console.log('WebSocket disconnected');
    }
  }
}
