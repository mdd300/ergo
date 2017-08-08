package com.uniquesys.qrgo.Chat;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.MainActivity;
import com.uniquesys.qrgo.Produtos.GridActivity;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.Base64Custom;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;
import com.uniquesys.qrgo.model.Imagem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {

    private static final String PREF_NAME = "USER_LOG";
    public String user_id;
    public String hash;
    private DatabaseReference firebase;
    private Query firebaseLast;
    List<Bitmap> splittedBitmaps = new ArrayList<>();
    List<String> splittedid = new ArrayList<>();
    Bitmap resul;
    ValueEventListener valueEventListenerMensagem;
    ValueEventListener valueEventListenerLastMensagem;
    List<String> LastMessage = new ArrayList<>();
    String LMessage;
    Bitmap img;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

        new NotificationConversa().Ativado(this,getResources(),user_id,"desativar");

        if(user_id != "" && hash != ""){
            String function = "hash_user";
            String user = user_id;
            String hash_user = hash;
            String method = "http://uniquesys.jelasticlw.com.br/qrgo/login/verificar_login_app";
            Model loginTask = new Model(this);
            loginTask.execute(function,method, user, hash_user);
            String result = null;
            try {
                result = loginTask.get();

                if(result.equals("true")){

                    valueEventListenerLastMensagem = new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {

                                for (DataSnapshot mes : dataSnapshot.getChildren()) {

                                    splittedid.add(dataSnapshot.getKey());

                                    String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/assets/admin/images/avtar/user.png";
                                    String method2 = urlOfImage;
                                    String function2 = "imagem";
                                    Imagem imgTask = new Imagem();
                                    imgTask.execute(function2, method2);
                                    try {
                                        resul = imgTask.get();
                                        splittedBitmaps.add(resul);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                    String MensagemRecebida = mes.getValue().toString();
                                    MensagemRecebida = Base64Custom.decodificarBase64(MensagemRecebida);
                                    String[] separated = MensagemRecebida.split("#Controle#QRGO2017#Bolacha#");
                                    LMessage = separated[1].toString();
                                    LastMessage.add(LMessage);

                                    Fragment fragment = null;

                                    if (!isFinishing()) {
                                        fragment = new ListViewFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                                        bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                                        bundle.putStringArrayList("LastMessage", (ArrayList<String>) LastMessage);
                                        fragment.setArguments(bundle);
                                        getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.ListChat, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }

                    };


                    valueEventListenerMensagem = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            splittedBitmaps.clear();
                            splittedid.clear();
                            LastMessage.clear();

                                for (DataSnapshot key : dataSnapshot.getChildren()) {

                                    firebaseLast =  ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos").child(key.getKey()).child(key.getKey()).orderByKey().limitToLast(1);

                                    firebaseLast.addValueEventListener(valueEventListenerLastMensagem);


                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };

                    firebase = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");

                    firebase.addValueEventListener(valueEventListenerMensagem);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }else{
            Intent intent = new Intent(ChatActivity.this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    public void contatos(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ChatActivity.this,ContatosActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_right,R.anim.anim_slide_left);
        finish();
    }

    public void grid(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ChatActivity.this,GridActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
    }
    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
        firebaseLast.removeEventListener(valueEventListenerLastMensagem);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        firebase.addValueEventListener(valueEventListenerMensagem);
        firebaseLast.addValueEventListener(valueEventListenerLastMensagem);
    }

}
