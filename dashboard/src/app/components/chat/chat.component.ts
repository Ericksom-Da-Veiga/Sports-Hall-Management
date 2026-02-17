// src/app/components/chat/chat.component.ts
import { Component, OnInit } from '@angular/core';
import { MessagesService, Message } from 'src/app/services/messages/messages.service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css']
})
export class ChatComponent implements OnInit {

  messages: Message[] = [];
  user1 = 1; // exemplo: utilizador logado
  user2 = 6; // exemplo: utilizador do chat

  constructor(private messagesService: MessagesService) {}

  ngOnInit(): void {
    this.loadConversation();
  }

  loadConversation() {
    this.messagesService.getConversation(this.user1, this.user2)
      .subscribe({
        next: (msgs) => this.messages = msgs,
        error: (err) => console.error(err)
      });
  }
}
