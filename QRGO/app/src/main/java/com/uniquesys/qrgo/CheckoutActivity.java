package com.uniquesys.qrgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class CheckoutActivity extends AppCompatActivity {
    private static final String PREF_NAME = "USER_LOG";
    String hash;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

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

    }
}
