import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdherantResponse, AdherantService } from 'src/app/services/adherant/adherant.service';


@Component({
  selector: 'app-form-adherant',
  templateUrl: './form-adherant.component.html',
  styleUrls: ['./form-adherant.component.scss']
})

export class FormAdherantComponent {
  message!: String;
  error!: string;
  
  cin!: String;
  nom!: String;
  prenom!: String;
  mail!: String;
  password!: String;
  date_naissance!: Date| null;
  adress!: String;
  telephone!: String;
  ville!: String;
  nom_pere!: String;
  nom_mere!: String;
  tel_parant!: String;

  maxDate!: string;

  constructor(private AdherantService: AdherantService){
    this.maxDate = '2014-12-31';
  };

  saveAdherant() {

    var inputdata = {
      cin : this.cin,
      nom : this.nom,
      prenom : this.prenom,
      mail : this.mail,
      password : this.password,
      date_naissance : this.date_naissance,
      adress : this.adress,
      telephone : this.telephone,
      ville : this.ville,
      nom_pere : this.nom_pere,
      nom_mere : this.nom_mere,
      tel_parant :this.tel_parant
    }

    this.AdherantService.SaveAdherant(inputdata).subscribe({
      next: (res : any) => {
        if(res.data != null){
          this.error = "";
          this.message = "Inscrição bem sucedida";
          this.cin ="";
          this.nom = "";
          this.prenom = "";
          this.mail = "";
          this.password = "";
          this.adress = "";
          this.telephone = "";
          this.date_naissance = null;
          this.ville = "";
          this.nom_pere = "";
          this.nom_mere = "";
          this.tel_parant = "";
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