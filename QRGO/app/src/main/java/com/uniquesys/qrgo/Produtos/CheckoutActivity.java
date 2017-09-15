package com.uniquesys.qrgo.Produtos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.Chat.ChatActivity;
import com.uniquesys.qrgo.Clientes.ClientesActivity;
import com.uniquesys.qrgo.MainActivity;
import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;

import java.util.concurrent.ExecutionException;

public class CheckoutActivity extends AppCompatActivity {
    private static final String PREF_NAME = "USER_LOG";
    String hash;
    String user_id;
    DatabaseReference firebaseLast;
    ValueEventListener valueEventListenerLastMensagemNot;
    int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        valueEventListenerLastMensagemNot = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (i > 0) {
                NotificationManager nm = (NotificationManager) CheckoutActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent p = PendingIntent.getActivity(CheckoutActivity.this, 0, new Intent(CheckoutActivity.this, ChatActivity.class), 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(CheckoutActivity.this);

                builder.setTicker("Nova Mensagem");
                builder.setContentTitle("QRGO");
                builder.setContentText("Nova Mensagem");
                builder.setSmallIcon(R.drawable.logo);
                builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
                builder.setContentIntent(p);

                Notification n = builder.build();
                n.vibrate = new long[]{150, 300, 150, 600};
                nm.notify(R.drawable.logo, n);

                try{
                    Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone toque = RingtoneManager.getRingtone(CheckoutActivity.this,som);
                    toque.play();
                }catch (Exception e){

                }}
                    i++;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

        firebaseLast = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");


        firebaseLast.addValueEventListener(valueEventListenerLastMensagemNot);

        WebView webview = (WebView) findViewById(R.id.carrinho_wv);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webview.setWebViewClient(new WebViewClient());

        String postData = "user_id=" + user_id;
        webview.postUrl("http://uniquesys.jelasticlw.com.br/qrgo/pedidos/carrinho_app", postData.getBytes());

    }

    public void produtos(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(CheckoutActivity.this, GridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    public void camera(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(CheckoutActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }


    public void limpar(View v) throws ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        String codigo = user_id;
        String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/limparCarrinho_app";
        String function = "produto";
        Model prodTask = new Model(this);
        prodTask.execute(function,method, codigo);
        String resul = null;

        try {

            resul = prodTask.get();
            WebView webview = (WebView) findViewById(R.id.carrinho_wv);
            WebSettings webSettings = webview.getSettings();
            webSettings.setJavaScriptEnabled(true);

            webview.setWebViewClient(new WebViewClient());

            String postData = "user_id=" + user_id;
            webview.postUrl("http://uniquesys.jelasticlw.com.br/qrgo/pedidos/carrinho_app", postData.getBytes());
            Toast toast = Toast.makeText(getApplicationContext(), "Seu carrinho foi limpo com sucesso",Toast.LENGTH_SHORT);
            toast.show();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
    }

    public void guardar(View v) throws ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        String codigo = user_id;
        String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/Guardar_carrinho_app";
        String function = "user_id";
        Model prodTask = new Model(this);
        prodTask.execute(function,method, codigo);
        String resul = null;

        try {

            resul = prodTask.get();
            Log.e("guardar",resul);
            Toast toast = Toast.makeText(getApplicationContext(), "Seu carrinho foi guardado com sucesso",Toast.LENGTH_SHORT);
            toast.show();
        } catch (ExecutionException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CheckoutActivity.this, GridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void chat(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(CheckoutActivity.this,ChatActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }
    public void Clientes(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(CheckoutActivity.this,ClientesActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_right_leave, R.anim.anim_slide_left_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }
}
