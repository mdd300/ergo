package com.uniquesys.qrgo.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.MainActivity;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;
import com.uniquesys.qrgo.model.Imagem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ChatActivity extends AppCompatActivity {

    private static final String PREF_NAME = "USER_LOG";
    public String user_id;
    public String hash;
    private DatabaseReference firebase;
    List<Bitmap> splittedBitmaps = new ArrayList<>();
    List<String> splittedid = new ArrayList<>();
    Bitmap resul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

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

                    firebase = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");

                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getValue() != null){

                                Map<String, String> teste = (Map<String, String>) dataSnapshot.getValue();

                                for (String key : teste.keySet()) {

                                    splittedid.add(key);

                                    String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/assets/admin/images/avtar/user.png";
                                    String method2 = urlOfImage;
                                    String function2 = "imagem";
                                    Imagem imgTask = new Imagem();
                                    imgTask.execute(function2, method2);

                                    try {

                                        resul = imgTask.get();

                                        splittedBitmaps.add(resul);

                                        Display display = getWindowManager().getDefaultDisplay();
                                        Point size = new Point();
                                        display.getSize(size);
                                        int width = size.x;
                                        Fragment fragment = null;

                                        if (width >= 720) {
                                            fragment = new ListViewFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                                            bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                                            fragment.setArguments(bundle);
                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.ListChat, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                        }

                                        else
                                        {
                                            fragment =  new ListViewFragment();
                                            Bundle bundle = new Bundle();
                                            bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                                            bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                                            fragment.setArguments(bundle);
                                            getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.ListChat, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                                        }

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    } catch (ExecutionException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
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
}
