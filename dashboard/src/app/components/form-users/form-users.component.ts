import { Component } from '@angular/core';
import { UserResponse, UserService } from 'src/app/services/user/user.service';


@Component({
  selector: 'app-form-users',
  templateUrl: './form-users.component.html',
  styleUrls: ['./form-users.component.scss']
})

export class FormUsersComponent {
  message!: String;
  error!: string;
  
  cin!: String;
  nom!: String;
  prenom!: String;
  mail!: String;
  password!: String;
  telephone!: String;
  role!: String;

  roles: string[] = ['Admin', 'User', 'SuperAdmin'];

  constructor(private UserService: UserService){};

  saveUser() {

    var inputdata = {
      cin : this.cin,
      nom : this.nom,
      prenom : this.prenom,
      mail : this.mail,
      password : this.password,
      telephone : this.telephone,
      role : this.role
    }

    this.UserService.SaveUser(inputdata).subscribe({
      next: (res : any) => {
        if(res.data != null){
          this.error = "";
          this.message = "Utilizador registado com sucesso";
          this.cin ="";
          this.nom = "";
          this.prenom = "";
          this.mail = "";
          this.password = "";
          this.telephone = "";
          this.role = "";
        }else{
          this.error = res.message;
          this.message = "";
        }
      },error:(err: any) => {
          this.message = "";
          this.error = "Verifique as informações inseridas e tente novamente";
        },
    });
  }
}
