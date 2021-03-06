package com.uniquesys.qrgo.Produtos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.support.v7.widget.AppCompatImageView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.Chat.ChatActivity;
import com.uniquesys.qrgo.Chat.NotificationConversa;
import com.uniquesys.qrgo.Clientes.ClientesActivity;
import com.uniquesys.qrgo.Produtos.CheckOut.CheckOutActivity;
import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;
import com.uniquesys.qrgo.model.Imagem;
import com.uniquesys.qrgo.MainActivity;
import com.uniquesys.qrgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ProdutoActivity extends AppCompatActivity {

    private static final String PREF_NAME = "USER_LOG";
    private NotificationConversa Not;

    String id;
    Context ctx;
    String method;
    String function;
    String resul = null;

    String PPIDS;
    String PIDS;
    String MIDS;
    String GIDS;
    String GGIDS;

    String user_id;
    String hash;
    String imgJ;
    String resultado;

    ArrayList idProd = new ArrayList();
    int i;
    int arrayleght;
    int j;

    DatabaseReference firebaseLast;
    ValueEventListener valueEventListenerLastMensagemNot;

    final GestureDetector gestureDetector = new GestureDetector(ctx, new GestureListener());

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_DISTANCE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();

            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    try {
                        onSwipeRight();

                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        onSwipeLeft();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                return true;
            }
            return false;
        }
    }

    public void onSwipeRight() throws ExecutionException, InterruptedException {
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rL3);
        ViewFlipper simpleViewFlipper=(ViewFlipper) rl3.findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showNext();

        ViewFlipper simpleViewFlipperColor=(ViewFlipper) findViewById(R.id.relativeLayout4);
        simpleViewFlipperColor.showNext();

        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
        simpleViewFlipperNome.showNext();

        arrayleght = idProd.size();
        i++;
        if(i == arrayleght ){
            String idPesquisa = idProd.get(0).toString();
            i = 0;
            String methodS = "http://192.168.0.85/erp/vendas_produtos/getVariantesTamanho";
            this.estoque(idPesquisa,methodS,i+1);
            EditText edit0 = (EditText) findViewById(R.id.edit0);
            edit0.setText("");
            EditText edit1 = (EditText) findViewById(R.id.edit1);
            edit1.setText("");
            EditText edit2 = (EditText) findViewById(R.id.edit2);
            edit2.setText("");
            EditText edit3 = (EditText) findViewById(R.id.edit3);
            edit3.setText("");
            EditText edit4 = (EditText) findViewById(R.id.edit4);
            edit4.setText("");

        }else{
            String idPesquisa = idProd.get(i).toString();
            String methodS = "http://192.168.0.85/erp/vendas_produtos/getVariantesTamanho";
            this.estoque(idPesquisa,methodS,i+1);
            EditText edit0 = (EditText) findViewById(R.id.edit0);
            edit0.setText("");
            EditText edit1 = (EditText) findViewById(R.id.edit1);
            edit1.setText("");
            EditText edit2 = (EditText) findViewById(R.id.edit2);
            edit2.setText("");
            EditText edit3 = (EditText) findViewById(R.id.edit3);
            edit3.setText("");
            EditText edit4 = (EditText) findViewById(R.id.edit4);
            edit4.setText("");

        }

    }

    public void onSwipeLeft() throws ExecutionException, InterruptedException {
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rL3);
        ViewFlipper simpleViewFlipper=(ViewFlipper) rl3.findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showPrevious();

        ViewFlipper simpleViewFlipperColor=(ViewFlipper) findViewById(R.id.relativeLayout4);
        simpleViewFlipperColor.showPrevious();

        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
        simpleViewFlipperNome.showPrevious();

        arrayleght = idProd.size();
        i--;
        if(i < 0){
            i = arrayleght - 1;
            String idPesquisa = idProd.get(i).toString();
            String methodS = "http://192.168.0.85/erp/vendas_produtos/getVariantesTamanho";
            this.estoque(idPesquisa,methodS,i+1);
            EditText edit0 = (EditText) findViewById(R.id.edit0);
            edit0.setText("");
            EditText edit1 = (EditText) findViewById(R.id.edit1);
            edit1.setText("");
            EditText edit2 = (EditText) findViewById(R.id.edit2);
            edit2.setText("");
            EditText edit3 = (EditText) findViewById(R.id.edit3);
            edit3.setText("");
            EditText edit4 = (EditText) findViewById(R.id.edit4);
            edit4.setText("");
        }else{
            String idPesquisa = idProd.get(i).toString();
            String methodS = "http://192.168.0.85/erp/vendas_produtos/getVariantesTamanho";
            this.estoque(idPesquisa,methodS,i+1);
            EditText edit0 = (EditText) findViewById(R.id.edit0);
            edit0.setText("");
            EditText edit1 = (EditText) findViewById(R.id.edit1);
            edit1.setText("");
            EditText edit2 = (EditText) findViewById(R.id.edit2);
            edit2.setText("");
            EditText edit3 = (EditText) findViewById(R.id.edit3);
            edit3.setText("");
            EditText edit4 = (EditText) findViewById(R.id.edit4);
            edit4.setText("");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);

        final SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

        Intent intent = getIntent();

        EditText CampoID = (EditText) findViewById(R.id.TextId);
        CampoID.setVisibility(View.INVISIBLE);

        valueEventListenerLastMensagemNot = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (j > 0) {
                NotificationManager nm = (NotificationManager) ProdutoActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
                PendingIntent p = PendingIntent.getActivity(ProdutoActivity.this, 0, new Intent(ProdutoActivity.this, ChatActivity.class), 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(ProdutoActivity.this);

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
                    Ringtone toque = RingtoneManager.getRingtone(ProdutoActivity.this,som);
                    toque.play();
                }catch (Exception e){

                }}
                i++;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        firebaseLast = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");


        firebaseLast.addValueEventListener(valueEventListenerLastMensagemNot);


        if(savedInstanceState != null){
            resultado = savedInstanceState.getString("resultadoProduto");

        }else {

            Bundle bundle = intent.getExtras();

            resultado = bundle.getString("resultado");
            Log.e("resultado", resultado);
        }

        try {

            JSONArray JASresult = new JSONArray(resultado.toString());
            JSONObject obj = JASresult.getJSONObject(0);
            String nome = obj.getString("prod_desc");
            String preco = obj.getString("prod_preco");
            String ref = "prod_ref";
            imgJ = obj.getString("img_nome");
            id = obj.getString("prod_id");
            Log.e("idProd",id);

            TextView txtProd = (TextView)findViewById(R.id.txtProd);
            txtProd.setText(nome);
            TextView txtPreco = (TextView)findViewById(R.id.txtPreco);
            txtPreco.setText(preco);
            TextView txtRef = (TextView)findViewById(R.id.txtReferencia);
            txtRef.setText(ref);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String codigo = id;
        method = "http://192.168.0.85/erp/vendas_produtos/getVariantes";
        function = "produto";
        Model prodTask = new Model(this);
        prodTask.execute(function,method,codigo,user_id,hash,"prod_id_fk");
        String resul = null;

        try {

            resul = prodTask.get();
            Log.e("corVariante",resul);
            JSONArray JASresultProd = new JSONArray(resul.toString());
            RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rL3);
            ViewFlipper simpleViewFlipper=(ViewFlipper) rl3.findViewById(R.id. relativeLayout3);
            if(JASresultProd.length() > 1   ) {
                simpleViewFlipper.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return gestureDetector.onTouchEvent(event);
                    }
                });
            }
            for(int k=0; k < JASresultProd.length();k++){

                JSONObject obj =  JASresultProd.getJSONObject(k);
                String img = obj.getString("img_nome");
                String idTeste = obj.getString("prod_id_fk");


                if(img != "null"){
                String urlOfImage = "http://192.168.0.85/erp/uploads/profiles/" + img;
                method = urlOfImage;
                function = "imagem";
                Imagem imgTask = new Imagem();
                imgTask.execute(function,method);
                    Bitmap result = null;
                try {
                    result  = imgTask.get();

                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(result);
                    simpleViewFlipper.addView(imageView);

                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper) findViewById(R.id.relativeLayout4);

                    ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
                    TextView TextViewNome = new TextView(this);
                    simpleViewFlipperNome.addView(TextViewNome);

                    simpleViewFlipperCollor = (ViewFlipper) findViewById(R.id.relativeLayout4);
                    AppCompatImageView imageViewCollor = new AppCompatImageView(this);

                    String color = obj.getString("cor_nome");
                    Log.w("cor",color);

                    Bitmap bit = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
                    bit.eraseColor(Color.parseColor(color));

                    int size = Math.min(bit.getWidth(), bit.getHeight());
                    int x = (bit.getWidth() - size) / 2;
                    int y = (bit.getHeight() - size) / 2;

                    Bitmap squared = Bitmap.createBitmap(bit, x, y, size, size);

                    Bitmap corBit = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(corBit);
                    Paint paint = new Paint();
                    paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
                    paint.setAntiAlias(true);
                    float r = size / 2f;
                    canvas.drawCircle(r, r, r, paint);

                    imageViewCollor.setImageBitmap(corBit);
                    imageViewCollor.setBackground(getResources().getDrawable(R.drawable.corprod));
                    simpleViewFlipperCollor.addView(imageViewCollor);
                    simpleViewFlipperNome = (ViewFlipper) findViewById(R.id.relativeLayout5);
                    TextViewNome = new TextView(this);


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else{

                    try{
                    String urlOfImage = "http://192.168.0.85/erp/uploads/profiles/" + imgJ;
                    method = urlOfImage;
                    function = "imagem";
                    Imagem imgTask = new Imagem();
                    imgTask.execute(function,method);
                    Bitmap result = null;
                    try {

                        result = imgTask.get();
                        ImageView imageView = new ImageView(this);
                        imageView.setImageBitmap(result);
                        simpleViewFlipper.addView(imageView);

                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper) findViewById(R.id.relativeLayout4);
                        AppCompatImageView imageViewCollor = new AppCompatImageView(this);

                    String color = obj.getString("cor_nome");
                        Log.w("cor",color);

                        Bitmap bit = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
                        bit.eraseColor(Color.parseColor(color));

                        int size = Math.min(bit.getWidth(), bit.getHeight());
                        int x = (bit.getWidth() - size) / 2;
                        int y = (bit.getHeight() - size) / 2;

                        Bitmap squared = Bitmap.createBitmap(bit, x, y, size, size);

                        Bitmap corBit = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

                        Canvas canvas = new Canvas(corBit);
                        Paint paint = new Paint();
                        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
                        paint.setAntiAlias(true);
                        float r = size / 2f;
                        canvas.drawCircle(r, r, r, paint);

                    imageViewCollor.setImageBitmap(corBit);
                        imageViewCollor.setBackground(getResources().getDrawable(R.drawable.corprod));
                    simpleViewFlipperCollor.addView(imageViewCollor);
                    ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
                    TextView TextViewNome = new TextView(this);



                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                this.idProd.add(idTeste.toString());

        final EditText PPE = (EditText)findViewById(R.id.edit0);
        final EditText PE = (EditText)findViewById(R.id.edit1);
        final EditText ME = (EditText)findViewById(R.id.edit2);
        final EditText GE = (EditText)findViewById(R.id.edit3);
        final EditText GGE = (EditText)findViewById(R.id.edit4);

                PPE.setOnKeyListener(new View.OnKeyListener()
                {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            switch (keyCode)
                            {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    if (PPE.getText().toString().matches("")) {
                                    }else {
                                        String PP = PPE.getText().toString();

                                        method = "http://192.168.0.85/erp/vendas_carrinhos/set_item_carrinho";
                                        function = "carrinho";
                                        Model estTask = new Model();
                                        estTask.execute(function, method, PPIDS, PP, id, user_id, hash);

                                        try {
                                            String res = estTask.get();
                                            Log.e("disponivel",res);
                                            JSONObject obj = new JSONObject(res.toString());
                                            String disponivel = obj.getString("disponivel");
                                            if (disponivel.equals("indisponivel")){
                                                Toast toast = Toast.makeText(getApplicationContext(), "Estoque insuficiente", Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                TextView PPD = (TextView) findViewById(R.id.Text0);
                                                Log.e("disponivel", disponivel);
                                                PPD.setText(disponivel);

                                                if (PP != "0") {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        } catch (JSONException e) {
                                            sharedPreferences.edit().clear().commit();

                                        }
                                    }
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });

                PE.setOnKeyListener(new View.OnKeyListener()
                {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            switch (keyCode)
                            {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    if (PE.getText().toString().matches("")) {
                                    }else{
                                        String P = PE.getText().toString();

                                        method = "http://192.168.0.85/erp/vendas_carrinhos/set_item_carrinho";
                                        function = "carrinho";
                                        Model estTask = new Model();
                                        estTask.execute(function, method, PIDS, P, id, user_id, hash);

                                        try {
                                            String res = estTask.get();
                                            JSONObject obj = new JSONObject(res.toString());
                                            String disponivel = obj.getString("disponivel");
                                            if (disponivel.equals("indisponivel")){
                                                Toast toast = Toast.makeText(getApplicationContext(), "Estoque insuficiente", Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                TextView PD = (TextView) findViewById(R.id.Text1);
                                                PD.setText(disponivel);
                                                if (P != "0") {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            }
                                        } catch (JSONException e) {

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });


                ME.setOnKeyListener(new View.OnKeyListener()
                                    {
                                        public boolean onKey(View v, int keyCode, KeyEvent event)
                                        {
                                            if (event.getAction() == KeyEvent.ACTION_DOWN)
                                            {
                                                switch (keyCode)
                                                {
                                                    case KeyEvent.KEYCODE_DPAD_CENTER:
                                                    case KeyEvent.KEYCODE_ENTER:
                                                        String M = ME.getText().toString();
                                                        if (ME.getText().toString().matches("")) {
                                                        }else {
                                                            method = "http://192.168.0.85/erp/vendas_carrinhos/set_item_carrinho";
                                                            function = "carrinho";
                                                            Model estTask = new Model();
                                                            estTask.execute(function, method, MIDS, M, id, user_id, hash);
                                                            try {
                                                                String res = estTask.get();
                                                                JSONObject obj = new JSONObject(res.toString());
                                                                String disponivel = obj.getString("disponivel");
                                                                if (disponivel.equals("indisponivel")){
                                                                    Toast toast = Toast.makeText(getApplicationContext(), "Estoque insuficiente", Toast.LENGTH_SHORT);
                                                                    toast.show();
                                                                } else {
                                                                    TextView MD = (TextView) findViewById(R.id.Text2);
                                                                    Log.e("disponivel", disponivel);
                                                                    MD.setText(disponivel);
                                                                    if (M != "0") {
                                                                        Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso", Toast.LENGTH_SHORT);
                                                                        toast.show();
                                                                    }
                                                                }
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            } catch (ExecutionException e) {
                                                                e.printStackTrace();
                                                            } catch (JSONException e) {

                                                            }
                                                        }
                                                            return true;
                                                            default:
                                                                break;
                                                        }
                                                }
                                                return false;
                                            }
                                        });

                GE.setOnKeyListener(new View.OnKeyListener()
                {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            switch (keyCode)
                            {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    if (GE.getText().toString().matches("")) {
                                    }else{
                                        String G = GE.getText().toString();
                                        method = "http://192.168.0.85/erp/vendas_carrinhos/set_item_carrinho";
                                        function = "carrinho";
                                        Model estTask = new Model();
                                        estTask.execute(function, method, GIDS, G, id, user_id, hash);

                                        try {
                                            String res = estTask.get();
                                            JSONObject obj = new JSONObject(res.toString());
                                            String disponivel = obj.getString("disponivel");
                                            if (disponivel.equals("indisponivel")){
                                                Toast toast = Toast.makeText(getApplicationContext(), "Estoque insuficiente", Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                TextView GD = (TextView) findViewById(R.id.Text3);
                                                GD.setText(disponivel);
                                                if (G != "0") {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            }
                                        } catch (JSONException e) {


                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {

                                        }
                                    }
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });


                GGE.setOnKeyListener(new View.OnKeyListener()
                {
                    public boolean onKey(View v, int keyCode, KeyEvent event)
                    {
                        if (event.getAction() == KeyEvent.ACTION_DOWN)
                        {
                            switch (keyCode)
                            {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    String GG = GGE.getText().toString();
                                    if (GGE.getText().toString().matches("")) {
                                    }else{
                                        method = "http://192.168.0.85/erp/vendas_carrinhos/set_item_carrinho";
                                        function = "carrinho";
                                        Model estTask = new Model();
                                        estTask.execute(function, method, GGIDS, GG, id, user_id, hash);

                                        try {
                                            String res = estTask.get();
                                            JSONObject obj = new JSONObject(res.toString());
                                            String disponivel = obj.getString("disponivel");
                                            if (disponivel.equals("indisponivel")){
                                                Toast toast = Toast.makeText(getApplicationContext(), "Estoque insuficiente", Toast.LENGTH_SHORT);
                                                toast.show();
                                            } else {
                                                TextView GGD = (TextView) findViewById(R.id.Text4);
                                                GGD.setText(disponivel);
                                                if (GG != "0") {
                                                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso", Toast.LENGTH_SHORT);
                                                    toast.show();
                                                }
                                            }

                                        } catch (JSONException e) {

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        } catch (ExecutionException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                });

        }
            String idPesquisa;
            if(idProd.size() > 1)
                idPesquisa = idProd.get(1).toString();
            else
                idPesquisa = idProd.get(0).toString();
            String methodS = "http://192.168.0.85/erp/vendas_produtos/getVariantesTamanho";
            try {
                this.estoque(idPesquisa,methodS,2);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void grid(View v) throws ExecutionException, InterruptedException, JSONException {
        Intent intent = new Intent(ProdutoActivity.this, GridActivity.class);
        startActivity(intent);
        finish();
    }
    public void estoque(String idEstoque, String methodS,int i) throws ExecutionException, InterruptedException {

        function = "produtoEst";
        Model estTask = new Model();
        estTask.execute(function, methodS, idEstoque, user_id, hash, "prod_id_fk", String.valueOf(i));
        resul = estTask.get();
        if (resul.equals("[]")) {
            this.estoque(idEstoque, methodS, 1);
        } else {
            try {
                JSONArray JASresultEsPP = new JSONArray(resul.toString());
                JSONObject objPP = JASresultEsPP.getJSONObject(0);


                String PPSS = objPP.getString("estq_qtde");
                TextView PP = (TextView) findViewById(R.id.Text0);
                PPIDS = objPP.getString("prod_id");
                PP.setText(String.valueOf(PPSS));

                JSONArray JASresultEsP = new JSONArray(resul.toString());
                JSONObject objP = JASresultEsP.getJSONObject(1);

                String PSS = objP.getString("estq_qtde");
                TextView P = (TextView) findViewById(R.id.Text1);
                PIDS = objP.getString("prod_id");
                P.setText(String.valueOf(PSS));

                JSONArray JASresultEsM = new JSONArray(resul.toString());
                JSONObject objM = JASresultEsM.getJSONObject(2);

                String MSS = objM.getString("estq_qtde");
                TextView M = (TextView) findViewById(R.id.Text2);
                MIDS = objM.getString("prod_id");
                M.setText(String.valueOf(MSS));

                JSONArray JASresultEsG = new JSONArray(resul.toString());
                JSONObject objG = JASresultEsG.getJSONObject(3);

                String GSS = objG.getString("estq_qtde");
                TextView G = (TextView) findViewById(R.id.Text3);
                GIDS = objG.getString("prod_id");
                G.setText(String.valueOf(GSS));

                JSONArray JASresultEsGG = new JSONArray(resul.toString());
                JSONObject objGG = JASresultEsGG.getJSONObject(4);

                String GGSS = objGG.getString("estq_qtde");
                TextView GG = (TextView) findViewById(R.id.Text4);
                GGIDS = objGG.getString("prod_id");
                GG.setText(String.valueOf(GGSS));

            } catch (JSONException e) {
                Log.e("tag", e.getMessage());
                e.printStackTrace();
            }
        }
    }
    public void carrinho(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(ProdutoActivity.this, CheckOutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        resultado = null;
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    public void produtos(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(ProdutoActivity.this, GridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_right_leave,R.anim.anim_slide_left_leave);
        resultado = null;
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProdutoActivity.this, GridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_right_leave,R.anim.anim_slide_left_leave);
        resultado = null;
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("resultadoProduto",resultado);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void camera(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
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

    public void Logout(View v) throws ExecutionException, InterruptedException {
        SharedPreferences.Editor userPref = getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();
        userPref.clear();
        Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
    }
    public void chat(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ProdutoActivity.this,ChatActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }
    public void limpar(View v) throws ExecutionException, InterruptedException {
        EditText edit0 = (EditText) findViewById(R.id.edit0);
        edit0.setText("0");
        edit0.setText("");
        EditText edit1 = (EditText) findViewById(R.id.edit1);
        edit1.setText("0");
        edit1.setText("");
        EditText edit2 = (EditText) findViewById(R.id.edit2);
        edit2.setText("0");
        edit2.setText("");
        EditText edit3 = (EditText) findViewById(R.id.edit3);
        edit3.setText("0");
        edit3.setText("");
        EditText edit4 = (EditText) findViewById(R.id.edit4);
        edit4.setText("0");
        edit4.setText("");
    }
    public void Clientes(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ProdutoActivity.this,ClientesActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_right_leave, R.anim.anim_slide_left_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }
}
