import { HttpClient } from '@angular/common/http';
import { Component, OnInit, Input } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserResponse, UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-table-users',
  templateUrl: './table-users.component.html',
  styleUrls: ['./table-users.component.scss']
})

export class TableUsersComponent implements OnInit{
  @Input() showAdd: boolean = true;

  Users!: UserResponse[];
  data!: string;
  initialUsers!: UserResponse[]; //para guardar os utilizadores recuperados no inicio
// Variáveis para a paginação
  currentPage: number = 1;
  itemsPerPage: number = 10;
  number: number = 0;

  message!: string;

  constructor(
    private http: HttpClient, 
    private UserService: UserService,
    private route: Router,
    private activeroute: ActivatedRoute, 
  ) {}

  ngOnInit(): void {
    this.getUsers()
  }
  
  onPageChange(pageNumber: number) {
    this.currentPage = pageNumber;
  }

  getUsers() {
    this.UserService.getUsers().subscribe((res: any) => {
      this.Users = res.data;
      this.initialUsers = res.data
    })
  }

  chercherUser() {
    if (this.data.trim() === '') {
      this.Users = [...this.initialUsers];  // Restaura os dados iniciais
    } else {
      this.UserService.chercherUser(this.data).subscribe((res: any) =>{
          this.Users = res.data;
      })}; 
    }
  

  deleteUser(event: any, userId: any){
    if(confirm('Tem a certeza que deseja eliminar este utilizador?'))
      {
        this.UserService.deleteUser(userId).subscribe((resp:any)=>{
          this.message="Utilizador eliminado"
          setTimeout(() => window.location.reload(), 1500);
        })
      }
  }

  exportToCSV() {
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
  
    // Criando o conteúdo CSV
    let csv = '\ufeff'; // BOM para garantir que o Excel abra corretamente o arquivo UTF-8
  
    // Adicionando cabeçalhos
    csv += options.headers.join(options.fieldSeparator) + '\n';
  
    // Adicionando linhas de dados
    data.forEach(item => {
      // Type assertion para garantir que `item` corresponde ao formato esperado
      const row = options.headers.map(field => item[field as keyof typeof item]).join(options.fieldSeparator);
      csv += row + '\n';
    });
  
    // Criando um elemento 'a' invisível para baixar o arquivo
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement("a");
    if (link.download !== undefined) {
      const url = URL.createObjectURL(blob);
      link.setAttribute("href", url);
      link.setAttribute("download", "Utilizadores.csv");
      link.style.visibility = 'hidden';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    }
  }
  
}
