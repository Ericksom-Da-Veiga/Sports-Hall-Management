import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserPost, UserService } from 'src/app/services/user/user.service';

@Component({
  selector: 'app-edit-users',
  templateUrl: './edit-users.component.html',
  styleUrls: ['./edit-users.component.scss']
})
export class EditUsersComponent implements OnInit{
  user!: UserPost;
  userId!: any;
  message!: string;
  error!: string;
  roles: string[] = ['Admin', 'User', 'SuperAdmin'];

  constructor(
    private activeroute: ActivatedRoute, 
    private UserService: UserService,
    private route: Router
  ){};

  ngOnInit(){
    this.userId = this.activeroute.snapshot.paramMap.get('id');
    
    this.UserService.detailUser(this.userId).subscribe((res: any) => {
      this.user = res.data[0];
    })
  }

  updateUser(){

    var inputdata = {
      id : this.userId,
      cin : this.user.cin,
      nom : this.user.nom,
      prenom : this.user.prenom,
      mail : this.user.mail,
      password : this.user.password,
      telephone : this.user.telephone,
      role : this.user.role
      }

      this.UserService.UpdateUser(inputdata).subscribe({
        next: (res : any) => {
          if(res.data[0] != null){
            this.message="Utilizador modificado"
            setTimeout(() => this.route.navigate(['/users']), 1500);
          }else{
            this.error = "Problema ao modificar o utilizador"
          }
        }
      })
  }
}
