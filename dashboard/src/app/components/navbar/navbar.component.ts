import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserService, UserResponse } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  userName: string = '';

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const tokenUser: any = this.authService.getUserFromToken();
    let tokenEmail: string | null = null;

    if (tokenUser) {
      // basic name claims that might exist
      this.userName =
        tokenUser.nom ||           // custom claim
        tokenUser.name ||
        tokenUser.preferred_username ||
        tokenUser.username ||
        tokenUser.email ||
        tokenUser.sub ||
        '';

      // capture email for lookup
      if (typeof tokenUser.mail === 'string') {
        tokenEmail = tokenUser.mail;
      } else if (typeof tokenUser.email === 'string') {
        tokenEmail = tokenUser.email;
      } else if (typeof tokenUser.sub === 'string' && tokenUser.sub.includes('@')) {
        tokenEmail = tokenUser.sub;
      }
    }

    // if we don't yet have a full name, try to fetch it from the backend
    if (tokenEmail) {
      this.userService.getUsers().subscribe({
        next: (res: any) => {
          const allUsers: UserResponse[] = res.data;
          const me = allUsers.find(
            (u) => u.mail.toLowerCase() === tokenEmail!.toLowerCase()
          );
          if (me) {
            this.userName = `${me.nom} ${me.prenom}`;
          }
        },
        error: () => {
          // ignore; leave whatever name we already have
        }
      });
    }

    if (!this.userName) {
      this.userName = 'Usuário';
    }
  }
}
