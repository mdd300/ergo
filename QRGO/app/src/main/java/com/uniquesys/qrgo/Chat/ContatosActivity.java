package com.uniquesys.qrgo.Chat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.Clientes.ClientesActivity;
import com.uniquesys.qrgo.MainActivity;
import com.uniquesys.qrgo.Produtos.CheckOut.CheckOutActivity;
import com.uniquesys.qrgo.Produtos.CheckoutActivity;
import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;
import com.uniquesys.qrgo.model.Imagem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ContatosActivity extends AppCompatActivity {

    private static final String PREF_NAME = "USER_LOG";
    public String user_id;
    public String hash;
    private DatabaseReference firebase;
    List<Bitmap> splittedBitmaps = new ArrayList<>();
    List<String> splittedid = new ArrayList<>();
    Bitmap resul;
    ValueEventListener valueEventListenerMensagem;
    DatabaseReference firebaseLastM;
    ValueEventListener valueEventListenerLastMensagemNot;
    int j = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contatos);

        if(savedInstanceState != null){
            user_id = savedInstanceState.getString("user_id");
            hash = savedInstanceState.getString("hash");
            firebaseLastM.removeEventListener(valueEventListenerLastMensagemNot);
        }else {

            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);

            user_id = sharedPreferences.getString("user_id", "");
            hash = sharedPreferences.getString("hash", "");
        }

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

                    valueEventListenerMensagem = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            splittedBitmaps.clear();
                            splittedid.clear();

                            if(dataSnapshot.getValue() != null){

                                for (DataSnapshot key : dataSnapshot.getChildren()) {

                                    if(key.getKey() != user_id) {
                                        splittedid.add(key.getKey());

                                        String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/assets/admin/images/avtar/user.png";
                                        String method2 = urlOfImage;
                                        String function2 = "imagem";
                                        Imagem imgTask = new Imagem();
                                        imgTask.execute(function2, method2);
                                        try {
                                            resul = imgTask.get();
                                            splittedBitmaps.add(resul);

                                            Fragment fragment = null;

                                            if (!isFinishing()) {
                                                fragment = new ContatosFragment();
                                                Bundle bundle = new Bundle();
                                                bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                                                bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                                                fragment.setArguments(bundle);
                                                getSupportFragmentManager().beginTransaction()
                                                        .replace(R.id.ListContato, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                }
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
            Intent intent = new Intent(ContatosActivity.this, MainActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

    }

    public void chat(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ContatosActivity.this,ChatActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_fade_in);
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("user_id",user_id);
        outState.putString("hash",hash);

        j=0;

        valueEventListenerLastMensagemNot = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (j > 0) {
                    NotificationManager nm = (NotificationManager) ContatosActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                    PendingIntent p = PendingIntent.getActivity(ContatosActivity.this, 0, new Intent(ContatosActivity.this, ChatActivity.class), 0);

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ContatosActivity.this);

                    builder.setTicker("Nova Mensagem");
                    builder.setContentTitle("QRGO");
                    builder.setContentText("Nova Mensagem");
                    builder.setSmallIcon(R.drawable.logo);
                    builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
                    builder.setContentIntent(p);

                    Notification n = builder.build();
                    n.vibrate = new long[]{150, 300, 150, 600};
                    nm.notify(R.drawable.logo, n);

                    try {
                        Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone toque = RingtoneManager.getRingtone(ContatosActivity.this, som);
                        toque.play();
                    } catch (Exception e) {

                    }

                }
                j++;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        firebaseLastM = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");


        firebaseLastM.addValueEventListener(valueEventListenerLastMensagemNot);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseLastM != null)
            firebaseLastM.removeEventListener(valueEventListenerLastMensagemNot);
    }

    public void grid(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ContatosActivity.this,GridActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
    }
    public void carrinho(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ContatosActivity.this,CheckOutActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
    }
}
