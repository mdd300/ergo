package com.uniquesys.qrgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.Result;
import com.uniquesys.qrgo.Clientes.ClientesActivity;
import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    EditText user,senha;
    String login_name,login_pass;
    ZXingScannerView mScannerView;
    ProgressDialog pd;
    Context ctx;
    final GestureDetector gestureDetector = new GestureDetector(ctx, new GestureListener());
    private static final String PREF_NAME = "USER_LOG";
    String user_id = null;
    String hash = null;

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getY();
            float distanceY = e2.getY() - e1.getX();

            Log.e("posicao", String.valueOf(distanceX));
            Log.e("posicao",String.valueOf(distanceY));

            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceY < 0 && distanceX < 0) {
                    Intent intent = new Intent(MainActivity.this, GridActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
                        finish();

                    return true;
                }
            }
            return false;
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView cliente = (ImageView) findViewById(R.id.btnClientes);
        ImageView grid = (ImageView) findViewById(R.id.btnGrid);
        ImageView pedidos = (ImageView) findViewById(R.id.btnPedidos);
        mScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        cliente.setVisibility(View.INVISIBLE);
        grid.setVisibility(View.INVISIBLE);
        pedidos.setVisibility(View.INVISIBLE);
        mScannerView.setVisibility(View.INVISIBLE);

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

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

        if(user_id != "" && hash != ""){
            mScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
            mScannerView.setVisibility(View.VISIBLE);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
            cliente.setVisibility(View.VISIBLE);
            grid.setVisibility(View.VISIBLE);
            pedidos.setVisibility(View.VISIBLE);
            ImageView logo = (ImageView) findViewById(R.id.imageViewLogo);
            ImageView line = (ImageView) findViewById(R.id.imageView);
            RelativeLayout log = (RelativeLayout) findViewById(R.id.relative);
            logo.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            log.setVisibility(View.INVISIBLE);
            mScannerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
        }
    }
    public void login(View v) throws ExecutionException, InterruptedException, JSONException {

        user = (EditText)findViewById(R.id.txtUser);
        senha = (EditText) findViewById(R.id.txtSenha);
        login_name = user.getText().toString();
        login_pass = senha.getText().toString();
        String method = "http://192.168.0.85/erp/login/efetuarLogin";
        String function = "login";
        Model loginTask = new Model(this);
        loginTask.execute(function,method, login_name, login_pass);
        String result = loginTask.get();
        Log.e("result",result);
        JSONObject obj = new JSONObject (result.toString());
        String JAStuff = obj.getString("sucesso");
        if(JAStuff == "true"){

            String user_id = obj.getString("user_id");
            String hash = obj.getString("user_hash");

            SharedPreferences.Editor userPref = getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();
            userPref.putString("user_id",user_id);
            userPref.putString("hash",hash);
            userPref.apply();

// Tudo OK, podemos prosseguir.
            mScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
            mScannerView.setVisibility(View.VISIBLE);
            mScannerView.setResultHandler(this);
            mScannerView.startCamera();
            ImageView cliente = (ImageView) findViewById(R.id.btnClientes);
            ImageView grid = (ImageView) findViewById(R.id.btnGrid);
            ImageView pedidos = (ImageView) findViewById(R.id.btnPedidos);
            ImageView logo = (ImageView) findViewById(R.id.imageViewLogo);
            ImageView line = (ImageView) findViewById(R.id.imageView);
            RelativeLayout log = (RelativeLayout) findViewById(R.id.relative);
            logo.setVisibility(View.INVISIBLE);
            line.setVisibility(View.INVISIBLE);
            log.setVisibility(View.INVISIBLE);
            cliente.setVisibility(View.VISIBLE);
            grid.setVisibility(View.VISIBLE);
            pedidos.setVisibility(View.VISIBLE);
            mScannerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(), "Seu usuário ou senha está incorreta",Toast.LENGTH_SHORT);
            toast.show();
        }
    }




    @Override
    public void handleResult(Result result) {
        String[] separated = result.getText().split("readqrcodepedido/");
        String codigo = separated[1];
        String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/readqrcodepedido_app";
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


        Intent intent = new Intent(MainActivity.this, ProdutoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("resultado", resultado.toString());
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void Grid(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(MainActivity.this, GridActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up, R.anim.anim_slide_down);
        finish();
    }
    public void Clientes(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(MainActivity.this,ClientesActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_right_leave, R.anim.anim_slide_left_leave);
        finish();
    }
}
