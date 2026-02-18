import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';
import { UserResponse, UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-table-current-user',
  templateUrl: './table-current-user.component.html',
  styleUrls: ['./table-current-user.component.scss']
})
export class TableCurrentUserComponent implements OnInit {
  Users: UserResponse[] = [];
  data: string = '';
  initialUsers: UserResponse[] = [];
  currentPage = 1;
  itemsPerPage = 10;
  message = '';

  onPageChange(pageNumber: number) {
    this.currentPage = pageNumber;
  }

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    const tokenUser: any = this.authService.getUserFromToken();
    let id: number | null = null;
    if (tokenUser) {
      const raw = tokenUser.id ?? tokenUser.sub ?? null;
      const parsed = Number(raw);
      if (!isNaN(parsed)) {
        id = parsed;
      }
    }

    const loadById = (userId: number) => {
      this.userService.detailUser(userId).subscribe({
        next: (res: any) => {
          const u = res.data[0];
          if (u) {
            this.Users = [u];
            this.initialUsers = [u];
          }
        },
        error: () => {
          this.message = 'Erro ao carregar dados do utilizador';
        }
      });
    };

    if (id != null) {
      loadById(id);
    } else {
      // try email fallback similar to configuracao component
      let tokenEmail: string | null = null;
      const tokenUser: any = this.authService.getUserFromToken();
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
            if (me && me.id != null) {
              loadById(me.id);
            } else {
              this.message = 'Não foi possível localizar o utilizador';
            }
          },
          error: () => {
            this.message = 'Falha ao carregar lista de utilizadores';
          }
        });
      } else {
        this.message = 'ID do utilizador não encontrado no token';
      }
    }
  }

  // search/filter optional but works on array with one element
  chercherUser() {
    if (this.data.trim() === '') {
      this.Users = [...this.initialUsers];
    } else {
      // keep current row if it matches search text
      const lower = this.data.toLowerCase();
      this.Users = this.initialUsers.filter(u =>
        u.cin.toLowerCase().includes(lower) ||
        u.mail.toLowerCase().includes(lower) ||
        u.nom.toLowerCase().includes(lower) ||
        u.prenom.toLowerCase().includes(lower)
      );
    }
  }

  exportToCSV() {
    // same as table-users implementation
    const options = {
      fieldSeparator: ',',
      quoteStrings: '"',
      decimalseparator: '.',
      showLabels: true,
      headers: ["ID", "CIN", "Nome", "Apelido", "Email", "Telefone", "Role"]
    };

    const data = this.Users.map(user => ({
      "ID": user.id,
      "CIN": user.cin,
      "Nome": user.nom,
      "Apelido": user.prenom,
      "Email": user.mail,
      "Telefone": user.telephone,
      "Role": user.role
    }));

    let csv = '\ufeff';
    csv += options.headers.join(options.fieldSeparator) + '\n';
    data.forEach(item => {
      const row = options.headers.map(field => item[field as keyof typeof item]).join(options.fieldSeparator);
      csv += row + '\n';
    });
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement("a");
    if (link.download !== undefined) {
      const url = URL.createObjectURL(blob);
      link.setAttribute("href", url);
      link.setAttribute("download", "MeuUsuario.csv");
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  }
}
