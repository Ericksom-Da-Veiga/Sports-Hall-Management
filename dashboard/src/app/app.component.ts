import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ChatComponent } from 'src/app/components/chat/chat.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit{
  isLoginPage: boolean = false;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private dialog: MatDialog) { }

  ngOnInit(): void {
    this.router.events.subscribe(() => {
      this.isLoginPage = this.router.url.includes('login');
    });
  }

  openChat() {
    this.dialog.open(ChatComponent, {
      width: '400px',
      height: '600px',
      panelClass: 'chat-dialog'
    });
  }
}
