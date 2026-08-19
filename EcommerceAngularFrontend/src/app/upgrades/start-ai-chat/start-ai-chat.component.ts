import { Component, OnInit, EventEmitter, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClient } from "@angular/common/http";
import { environment } from "../../../environments/environment";
import { WebSocketService } from '../WebSocketService';
import { marked } from 'marked';

@Component({
  selector: 'app-start-ai-chat',
  templateUrl: './start-ai-chat.component.html',
  styleUrls: ['./start-ai-chat.component.css']
})
export class StartAiChatComponent implements OnInit {
  @Output() closeEvent = new EventEmitter<void>();

  close(): void {
    this.closeEvent.emit();
  }

  showToast = false;
  // Message in Chat
  messages: { type: 'sent' | 'received', text: string }[] = [];
  // Input field
  currentInput: string = '';
  errorMessage = '';
  isSubmitting = false;
  errorTimedOut = false;

  sessionId: string = '';

  constructor(private webSocket: WebSocketService, private http: HttpClient) {}

  ngOnInit(): void {
    // 1. Check if we already have a session ID and saved messages in sessionStorage
    let existingId = sessionStorage.getItem('chat_session_id');
    let savedMessages = sessionStorage.getItem('chat_messages');

    if (!existingId) {
      existingId = crypto.randomUUID();
      sessionStorage.setItem('chat_session_id', existingId);
    }

    this.sessionId = existingId;

    // Connect to WebSocket first
    this.webSocket.connectAiChat(this.sessionId, (payload) => {
      const normalized = payload.aiResponse
        .replace(/\\n/g, '\n')
        .replace(/\n{3,}/g, '\n\n');

      const renderer = new marked.Renderer();
      renderer.paragraph = (token: any) => `<div>${token.text}</div>`;

      const formatted: string = marked.parse(normalized, { renderer, async: false });

      this.messages.push({
        type: 'received',
        text: formatted
      });

      // Save updated messages to sessionStorage
      this.saveMessagesToSession();
      this.isSubmitting = false;
    });

    // 2. If we have saved messages from a previous opening, restore them!
    if (savedMessages) {
      this.messages = JSON.parse(savedMessages);
    } else {
      // 3. Otherwise, this is a brand new session, so trigger the initial conversation start
      this.currentInput = "Starting Conversation...";
      this.onSubmit();
    }
  }

  onSubmit() {
    if (!this.currentInput.trim()) return;

    const userText = this.currentInput;
    this.messages.push({ type: 'sent', text: userText });
    this.saveMessagesToSession();

    this.currentInput = '';
    this.isSubmitting = true;

    this.http.post(environment.apiUrl + '/aiChat', {
      currentInput: userText,
      sessionId: this.sessionId
    }).subscribe({
      error: (err) => {
        this.errorMessage = "Failed to send message";
        this.isSubmitting = false;
      }
    });
  }

  private saveMessagesToSession() {
    sessionStorage.setItem('chat_messages', JSON.stringify(this.messages));
  }
}
