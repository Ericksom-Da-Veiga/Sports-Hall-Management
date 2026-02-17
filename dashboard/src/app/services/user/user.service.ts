import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

export interface UserResponse{
  id: number;
  cin: string;
  mail: string;
  nom: string;
  prenom: string;
  telephone: string;
  role: string;
}

export interface UserPost{
  cin: string;
  mail: string;
  password: string;
  nom: string;
  prenom: string;
  telephone: string;
  role: string;
}
@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private Http: HttpClient
  ) { }

  getUsers() {
    return this.Http.get('http://localhost:8080/user');
  }

  chercherUser(data: String) {
    return this.Http.get(`http://localhost:8080/user/${data}`);
  }

  detailUser(useerID: number) {
    return this.Http.get(`http://localhost:8080/user/${useerID}/edit`);
  }
  
  GetUserByCIN(CIN: String) {
    return this.Http.get(`http://localhost:8080/user/get/${CIN}`);
  }

  SaveUser(inputData: object){
    return this.Http.post('http://localhost:8080/user', inputData);
  }

  UpdateUser(inputData: object){
    return this.Http.put('http://localhost:8080/user',inputData);
  }

  deleteUser(userID: number) {
    return this.Http.delete(`http://localhost:8080/user/${userID}`);
  }
}
