import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth/auth.service';

@Component({
  selector: 'app-sidenav',
  templateUrl: './sidenav.component.html',
  styleUrls: ['./sidenav.component.scss']
})
export class SidenavComponent implements OnInit{
  List!: any[];
  role: string | null = null;


  constructor(
    private HttpClient: HttpClient,
    private service: AuthService
  ){}

  ngOnInit(): void {
    this.role = this.service.getRole();

    this.HttpClient.get<any[]>('assets/SideBar.json').subscribe( sidebar=> {
      this.List = sidebar;

      //Enlever le Lien de Parametres au cas ou il n'est pas un superAdmin
      if (this.role !== 'SuperAdmin') {
        this.List = this.List.filter(item => item.name !== 'Paramètres');
      }

      // replace users item for non-admins with configuration link
      if (this.role !== 'Admin') {
        this.List = this.List.map(item => {
          if (item.routerLink === '/users') {
            return {
              ...item,
              name: 'Configuração',
              routerLink: '/configuracao'
            };
          }
          return item;
        });

        // also remove dashboard and sports links entirely
        this.List = this.List.filter(item => item.routerLink !== '/dashboard' && item.routerLink !== '/sports');
      }

      //verifier si il y a um item sauvegardé dans le sessionstotage
      const activeLinkId = localStorage.getItem('activeLinkId');
      if (activeLinkId && this.List) {
        const found = this.List.find(item => item.id === parseInt(activeLinkId));
        if (found) {
          this.activer(found);
        }
      }
    });
  }
  activeItem: any;

  activer(champ: any) {
    this.List.forEach(item => item.class = ''); // Remove 'active' de todos os itens
    champ.class = "active"

    //sauvegarder le item dans session storage
    localStorage.setItem('activeLinkId', champ.id.toString());
  }

  Logout(){
    this.service.logout();
  }
}
