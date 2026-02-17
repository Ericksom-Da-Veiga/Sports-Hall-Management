import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Message {
  id: number;
  sender_id: number;
  receiver_id: number;
  content: string;
  timestamp: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})


export class MessagesService {

   private apiUrl = 'http://localhost:8080/messages';

  constructor(private http: HttpClient) {}

  getConversation(user1: number, user2: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${this.apiUrl}/conversation?user1=${user1}&user2=${user2}`);
  }
}
