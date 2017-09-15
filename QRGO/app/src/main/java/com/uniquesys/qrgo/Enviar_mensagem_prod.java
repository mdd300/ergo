package com.uniquesys.qrgo;

import com.google.firebase.database.DatabaseReference;
import com.uniquesys.qrgo.config.Base64Custom;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;

/**
 * Created by Victor on 25/08/2017.
 */

public class Enviar_mensagem_prod {

    private DatabaseReference firebase;

    public  Enviar_mensagem_prod(){



    }

    public void enviar_mensagem(String destinatario, String remetente, String idProd){

        String mensagem = Base64Custom.codificarBase64(remetente + "#Controle#QRGO2017##Produto#QRGO2017#" + idProd);

        firebase = ConfiguracaoFirebase.getFirebase().child(remetente);

        firebase.child("Contatos")
                .child(destinatario)
                .setPriority(destinatario);

        firebase = ConfiguracaoFirebase.getFirebase().child(remetente);

        firebase.child("Contatos")
                .child(destinatario)
                .child(destinatario)
                .push()
                .setValue(mensagem);

        firebase = ConfiguracaoFirebase.getFirebase().child(destinatario);

        firebase.child("Contatos")
                .child(remetente)
                .setPriority(remetente);

        firebase = ConfiguracaoFirebase.getFirebase().child(destinatario);

        firebase.child("Contatos")
                .child(remetente)
                .child(remetente)
                .push()
                .setValue(mensagem);

    }

}
