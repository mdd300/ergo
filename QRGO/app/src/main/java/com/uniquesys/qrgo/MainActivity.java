package com.uniquesys.qrgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{
    EditText user,senha;
    String login_name,login_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void login(View v) {
        user = (EditText)findViewById(R.id.txtUser);
        senha = (EditText) findViewById(R.id.txtSenha);
        login_name = user.getText().toString();
        login_pass = senha.getText().toString();
        String method = "https://www.uniquesys.com.br/qrgo/login/efetuar_login";
        Model loginTask = new Model(this);
        loginTask.execute(method, login_name, login_pass);
    }

}
