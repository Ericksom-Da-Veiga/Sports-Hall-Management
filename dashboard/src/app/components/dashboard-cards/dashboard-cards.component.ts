import { Component, Input } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-dashboard-cards',
  templateUrl: './dashboard-cards.component.html',
  styleUrls: ['./dashboard-cards.component.scss']
})
export class DashboardCardsComponent {
  @Input() myclass!: string;
  @Input() titleh3!: string;
  @Input() text!: string;
  @Input() link!: string;

  isAdmin: boolean = false;

  constructor(private authService: AuthService) {
    this.isAdmin = this.authService.getRole() === 'Admin';
  }
}
