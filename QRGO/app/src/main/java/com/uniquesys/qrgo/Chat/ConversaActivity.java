package com.uniquesys.qrgo.Chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.Produtos.GridActivity;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.Base64Custom;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ConversaActivity extends AppCompatActivity {

    private static final String PREF_NAME = "USER_LOG";
    private DatabaseReference firebase;
    private DatabaseReference firebaseQ;
    String idUserRemetente;
    String idUserDestinatario;
    ArrayList<String> splittedMensagem = new ArrayList<>();
    ArrayList<String> splittedMensagemDes = new ArrayList<>();
    ValueEventListener valueEventListenerMensagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        if(savedInstanceState != null){
            idUserDestinatario = savedInstanceState.getString("Destinatario");
            idUserRemetente = savedInstanceState.getString("Remetente");

        }else {


            Intent intent = getIntent();

            Bundle bundle = intent.getExtras();

            idUserDestinatario = bundle.getString("id");

            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
            idUserRemetente = sharedPreferences.getString("user_id", "");
        }
        this.carregarMensagens();
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

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            Intent intent_next = new Intent(ConversaActivity.this, ChatActivity.class);
            startActivity(intent_next);
            overridePendingTransition(R.anim.anim_slide_right_leave, R.anim.anim_slide_left_leave);
            finish();
        new NotificationConversa().Ativado(this,getResources(),idUserRemetente,"desativar");
        }

    public void grid(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ConversaActivity.this,GridActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
        new NotificationConversa().Ativado(this,getResources(),idUserRemetente,"desativar");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("Destinatario",idUserDestinatario);
        outState.putString("Remetente",idUserRemetente);
    }

    public void mensagens(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ConversaActivity.this,ChatActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_left_leave,R.anim.anim_slide_right_leave);
        finish();
    }

public void carregarMensagens(){

    firebaseQ = ConfiguracaoFirebase.getFirebase().child(idUserRemetente)
            .child("Contatos")
            .child(idUserDestinatario)
            .child(idUserDestinatario);

    valueEventListenerMensagem = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if (dataSnapshot != null) {
                Fragment fragment = null;

                splittedMensagem.clear();
                splittedMensagemDes.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    String MensagemRecebida = dados.getValue().toString();
                    String MensagemDescod = Base64Custom.decodificarBase64(MensagemRecebida);
                    String[] separated = MensagemDescod.split("#Controle#QRGO2017#Bolacha#");
                    splittedMensagem.add(separated[1].toString());
                    if (separated[0].equals(idUserRemetente)) {
                        splittedMensagemDes.add("remetente");
                    } else {
                        splittedMensagemDes.add("destinatario");
                    }
                }
                if (!isFinishing()) {
                    fragment = new ConversaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("mensagem", splittedMensagem);
                    bundle.putStringArrayList("layout", splittedMensagemDes);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.ListMensagens, fragment, fragment.getClass().getSimpleName()).commitAllowingStateLoss();
                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    firebaseQ.addValueEventListener(valueEventListenerMensagem);
}
}


