import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserPost, UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-configuracao',
  templateUrl: './configuracao.component.html',
  styleUrls: ['./configuracao.component.scss']
})
export class ConfiguracaoComponent implements OnInit {
  user: any;
  userId: number | null = null;
  message: string = '';
  error: string = '';

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const tokenUser: any = this.authService.getUserFromToken();
    if (tokenUser) {
      const rawId = tokenUser.id ?? tokenUser.sub ?? null;
      const parsed = Number(rawId);
      if (!isNaN(parsed)) {
        this.userId = parsed;
      }
    }

    if (this.userId != null) {
      this.userService.detailUser(this.userId).subscribe({
        next: (res: any) => {
          this.user = res.data[0];
        },
        error: () => {
          this.error = 'Não foi possível carregar os seus dados';
        }
      });
    } else {
      // try to find user by email claim as fallback
      const tokenUser: any = this.authService.getUserFromToken();
      let tokenEmail: string | null = null;
      if (tokenUser) {
        tokenEmail =
          typeof tokenUser.mail === 'string' ? tokenUser.mail :
          typeof tokenUser.email === 'string' ? tokenUser.email :
          typeof tokenUser.sub === 'string' && tokenUser.sub.includes('@') ? tokenUser.sub :
          null;
      }
      if (tokenEmail) {
        this.userService.getUsers().subscribe({
          next: (res: any) => {
            const all: any[] = res.data;
            const me = all.find(u => u.mail.toLowerCase() === tokenEmail!.toLowerCase());
            if (me) {
              // backend response should include id
              this.userId = me.id;
              this.user = me;
            } else {
              this.error = 'Não foi possível localizar o utilizador';
            }
          },
          error: () => { this.error = 'Falha ao carregar lista de utilizadores'; }
        });
      } else {
        this.error = 'Identificador de utilizador não encontrado no token';
      }
    }
  }

  updateUser(): void {
    if (!this.userId) {
      return;
    }
    const inputdata = {
      id: this.userId,
      cin: this.user.cin,
      nom: this.user.nom,
      prenom: this.user.prenom,
      mail: this.user.mail,
      password: this.user.password,
      telephone: this.user.telephone,
      role: this.user.role // keep current role but field will be hidden
    };

    this.userService.UpdateUser(inputdata).subscribe({
      next: (res: any) => {
        if (res.data[0] != null) {
          this.message = 'Informações atualizadas';
        } else {
          this.error = 'Problema ao atualizar informações';
        }
      },
      error: (err) => {
        this.error = 'Erro ao comunicar com o servidor';
      }
    });
  }
}
