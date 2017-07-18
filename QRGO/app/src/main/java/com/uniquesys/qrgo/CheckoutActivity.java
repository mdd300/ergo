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

import org.json.JSONArray;
import org.json.JSONException;

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
        webview.postUrl("https://www.uniquesys.com.br/qrgo/pedidos/carrinho_app", postData.getBytes());

    }

    public void produtos(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(CheckoutActivity.this, GridActivity.class);
        startActivity(intent);

    }


    public void limpar(View v) throws ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        String codigo = user_id;
        String method = "https://www.uniquesys.com.br/qrgo/pedidos/limparCarrinho_app";
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
            webview.postUrl("https://www.uniquesys.com.br/qrgo/pedidos/carrinho_app", postData.getBytes());

        } catch (ExecutionException e){
            e.printStackTrace();
        }
    }

    public void guardar(View v) throws ExecutionException, InterruptedException {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        String user_id = sharedPreferences.getString("user_id", "");
        String codigo = user_id;
        String method = "https://www.uniquesys.com.br/qrgo/pedidos/Guardar_carrinho_app";
        String function = "user_id";
        Model prodTask = new Model(this);
        prodTask.execute(function,method, codigo);
        String resul = null;

        try {

            resul = prodTask.get();
            Log.e("guardar",resul);
        } catch (ExecutionException e){
            e.printStackTrace();
        }
    }
}
