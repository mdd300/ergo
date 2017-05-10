package com.uniquesys.qrgo;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    EditText user,senha;
    String login_name,login_pass;
    ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void login(View v) throws ExecutionException, InterruptedException, JSONException {
        user = (EditText)findViewById(R.id.txtUser);
        senha = (EditText) findViewById(R.id.txtSenha);
        login_name = user.getText().toString();
        login_pass = senha.getText().toString();
        String method = "https://www.uniquesys.com.br/qrgo/login/efetuar_login";
        Model loginTask = new Model(this);
        loginTask.execute(method, login_name, login_pass);
        String result = loginTask.get();
        JSONObject obj = new JSONObject (result.toString());
        String JAStuff = obj.getString("sucesso");
        if(JAStuff == "true"){
            mScannerView = new ZXingScannerView(this);
            setContentView(mScannerView);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
        }
    }

    @Override
    public void handleResult(Result result) {
        Log.e("Resultado",result.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan");
        builder.setMessage(result.getText());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
