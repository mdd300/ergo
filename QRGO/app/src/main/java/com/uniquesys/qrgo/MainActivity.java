package com.uniquesys.qrgo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    EditText user,senha;
    String login_name,login_pass;
    ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Verifica se já mostramos o alerta e o usuário negou na 1ª vez.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, CAMERA)) {
                // Caso o usuário tenha negado a permissão anteriormente, e não tenha marcado o check "nunca mais mostre este alerta"
                // Podemos mostrar um alerta explicando para o usuário porque a permissão é importante.
            } else {
                // Solicita a permissão
                ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 0);
            }
        } else {

        }
        setContentView(R.layout.activity_main);

    }
    public void login(View v) throws ExecutionException, InterruptedException, JSONException {
        user = (EditText)findViewById(R.id.txtUser);
        senha = (EditText) findViewById(R.id.txtSenha);
        login_name = user.getText().toString();
        login_pass = senha.getText().toString();
        String method = "https://www.uniquesys.com.br/qrgo/login/efetuar_login";
        String function = "login";
        Model loginTask = new Model(this);
        loginTask.execute(function,method, login_name, login_pass);
        String result = loginTask.get();
        JSONObject obj = new JSONObject (result.toString());
        String JAStuff = obj.getString("sucesso");
        if(JAStuff == "true"){
// Tudo OK, podemos prosseguir.
            mScannerView = new ZXingScannerView(this);
            setContentView(mScannerView);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();


        }
    }

    @Override
    public void handleResult(Result result) {


        String[] separated = result.getText().split("readqrcodepedido/");
        String codigo = separated[1];
        Log.e("Resultado",codigo);
        String method = "https://www.uniquesys.com.br/qrgo/pedidos/readqrcodepedido_app";
        String function = "produto";
        Model prodTask = new Model(this);
        prodTask.execute(function,method, codigo);
        String resultado = null;
        try {
            resultado = prodTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.e("Resultado",resultado.toString());

        Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);

        Bundle bundle = new Bundle();

        bundle.putString("resultado", resultado.toString());
        intent.putExtras(bundle);

        startActivity(intent);
    }
}
