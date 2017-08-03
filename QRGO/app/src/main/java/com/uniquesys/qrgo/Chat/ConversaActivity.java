package com.uniquesys.qrgo.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.Base64Custom;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ConversaActivity extends AppCompatActivity {

    private static final String PREF_NAME = "USER_LOG";
    private DatabaseReference firebase;
    String idUserRemetente;
    String idUserDestinatario;
    ArrayList<String> splittedMensagem = new ArrayList<>();
    ArrayList<String> splittedMensagemDes = new ArrayList<>();
    String codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        idUserDestinatario = bundle.getString("id");

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        idUserRemetente = sharedPreferences.getString("user_id", "");

        firebase = ConfiguracaoFirebase.getFirebase().child(idUserRemetente)
                .child("Contatos")
                .child(idUserDestinatario)
                .child(idUserDestinatario);

        firebase.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Fragment fragment = null;

                splittedMensagem.clear();
                splittedMensagemDes.clear();
                for(DataSnapshot dados: dataSnapshot.getChildren()) {

                    String MensagemRecebida = dados.getValue().toString();
                    String MensagemDescod = Base64Custom.decodificarBase64(MensagemRecebida);
                    String[] separated = MensagemDescod.split("#Controle#QRGO2017#Bolacha#");
                    splittedMensagem.add(separated[1].toString());
                    Log.e("teste",separated[0] + "  " + separated[1]);
                    if(separated[0].equals(idUserRemetente)) {
                        Log.e("teste","teste");
                        splittedMensagemDes.add("remetente");
                    }else {
                        splittedMensagemDes.add("destinatario");
                    }
                    }

                        fragment = new ConversaFragment();
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("mensagem", splittedMensagem);
                        bundle.putStringArrayList("layout", splittedMensagemDes);
                        fragment.setArguments(bundle);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.ListMensagens, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void Enviar(View v) throws ExecutionException, InterruptedException {

        EditText EditMensagem = (EditText) findViewById(R.id.MensagemParaEnviar);
        String Mensagem = EditMensagem.getText().toString();
        String Mensagemcod =  Base64Custom.codificarBase64(idUserRemetente+ "#Controle#QRGO2017#Bolacha#" +Mensagem);

        if(Mensagem.isEmpty()){

        }else{
            firebase = ConfiguracaoFirebase.getFirebase().child(idUserRemetente);

            firebase.child("Contatos")
                    .child(idUserDestinatario)
                    .setPriority(idUserDestinatario);

            firebase = ConfiguracaoFirebase.getFirebase().child(idUserRemetente);

            firebase.child("Contatos")
                    .child(idUserDestinatario)
                    .child(idUserDestinatario)
                    .push()
                    .setValue(Mensagemcod);

            firebase = ConfiguracaoFirebase.getFirebase().child(idUserDestinatario);

            firebase.child("Contatos")
                    .child(idUserRemetente)
                    .setPriority(idUserRemetente);

            firebase = ConfiguracaoFirebase.getFirebase().child(idUserDestinatario);

            firebase.child("Contatos")
                    .child(idUserRemetente)
                    .child(idUserRemetente)
                    .push()
                    .setValue(Mensagemcod);
            
            EditMensagem.setText("");
        }

    }


}
