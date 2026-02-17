package com.salle.sport.infra;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//classe responsavel pela estruturaçao das respostas enviadas do back-end para o front-end
public class Response<T> {

    private String message;
    private String status;
    private List<T> data;

    //constructor
    public Response(String message, String status) {
        this.message = message;
        this.status = status;
    }

    //Fait le retour des donnée en cas de succes
    public void success(String message, String status, List<T> data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    //Fait le  retour en cas de panne ou autres problemes au niveau du code
    public void exception(String message, String status) {
        this.message = message;
        this.status = status;
        this.data = null;
    }

    //Fait le retour en cas de probleme dans la request du front-end
    public void error(String message, String status) {
        this.message = message;
        this.status = status;
        this.data = null;
    }
}
