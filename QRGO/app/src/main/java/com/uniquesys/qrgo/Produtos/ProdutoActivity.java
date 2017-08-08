package com.uniquesys.qrgo.Produtos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.uniquesys.qrgo.Chat.NotificationConversa;
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
    Bitmap image;
    Context ctx;
    String method;
    String function;
    String resul = null;
    JSONArray PPID;
    JSONArray PID;
    JSONArray MID;
    JSONArray GID;
    JSONArray GGID;

    String PPIDS;
    String PIDS;
    String MIDS;
    String GIDS;
    String GGIDS;

    String user_id;
    String hash;
    String imgJ;

    ArrayList idProd = new ArrayList();
    int i;
    int arrayleght;

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
        ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showNext();

        ViewFlipper simpleViewFlipperColor=(ViewFlipper)findViewById(R.id.relativeLayout4);
        simpleViewFlipperColor.showNext();

        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
        simpleViewFlipperNome.showNext();

        arrayleght = idProd.size();
        i++;
        if(i == arrayleght ){
            String idPesquisa = idProd.get(0).toString();
            i = 0;
            String methodS = "http://uniquesys.jelasticlw.com.br/qrgo/produtos/prod_estoque_app";
            this.estoque(idPesquisa,methodS);

        }else{
            String idPesquisa = idProd.get(i).toString();
            String methodS = "http://uniquesys.jelasticlw.com.br/qrgo/produtos/prod_estoque_app";
            this.estoque(idPesquisa,methodS);

        }

    }

    public void onSwipeLeft() throws ExecutionException, InterruptedException {
        ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showPrevious();

        ViewFlipper simpleViewFlipperColor=(ViewFlipper)findViewById(R.id.relativeLayout4);
        simpleViewFlipperColor.showPrevious();

        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
        simpleViewFlipperNome.showPrevious();

        arrayleght = idProd.size();
        i--;
        if(i < 0){
            i = arrayleght - 1;
            String idPesquisa = idProd.get(i).toString();
            String methodS = "http://uniquesys.jelasticlw.com.br/qrgo/produtos/prod_estoque_app";
            this.estoque(idPesquisa,methodS);
        }else{
            String idPesquisa = idProd.get(i).toString();
            String methodS = "http://uniquesys.jelasticlw.com.br/qrgo/produtos/prod_estoque_app";
            this.estoque(idPesquisa,methodS);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Intent intent = getIntent();

        EditText CampoID = (EditText) findViewById(R.id.TextId);
        CampoID.setVisibility(View.INVISIBLE);

        Bundle bundle = intent.getExtras();

        String resultado = bundle.getString("resultado");

        try {

            JSONArray JASresult = new JSONArray(resultado.toString());
            JSONObject obj = JASresult.getJSONObject(0);
            String nome = obj.getString("prod_text");
            String preco = obj.getString("prod_preco");
            String ref = obj.getString("prod_ref");
            imgJ = obj.getString("img_nome");
            id = obj.getString("prod_id");

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
        method = "http://uniquesys.jelasticlw.com.br/qrgo/produtos/prod_app";
        function = "produto";
        Model prodTask = new Model(this);
        prodTask.execute(function,method, codigo);
        String resul = null;

        try {

            resul = prodTask.get();
            JSONArray JASresultProd = new JSONArray(resul.toString());
            ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
            simpleViewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });

            for(int j=0; j < JASresultProd.length();j++){

                JSONObject obj =  JASresultProd.getJSONObject(j);
                String img = obj.getString("img_nome");
                String idTeste = obj.getString("prod_id");

                if(img != "null"){
                String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/uploads/produtos/img/" + img;
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

                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper)findViewById(R.id.relativeLayout4);
                    ImageView imageViewCollor = new ImageView(this);
                    simpleViewFlipperCollor.addView(imageViewCollor);

                    ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
                    TextView TextViewNome = new TextView(this);
                    simpleViewFlipperNome.addView(TextViewNome);

                    this.idProd.add(idTeste.toString());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else{

                    try{
                    String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/uploads/produtos/img/" + imgJ;
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

                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper)findViewById(R.id.relativeLayout4);
                    ImageView imageViewCollor = new ImageView(this);
                    String color = obj.getString("prod_color");
                    imageViewCollor.setBackgroundColor(Color.parseColor(color));
                    simpleViewFlipperCollor.addView(imageViewCollor);

                    ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
                    TextView TextViewNome = new TextView(this);
                    String cor_nome = obj.getString("cor_nome");
                    TextViewNome.setText(cor_nome);
                    simpleViewFlipperNome.addView(TextViewNome);

                    this.idProd.add(idTeste.toString());

                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

        String idPesquisa = idProd.get(0).toString();
        String methodS = "http://uniquesys.jelasticlw.com.br/qrgo/produtos/prod_estoque_app";
        try {
            this.estoque(idPesquisa,methodS);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final EditText PPE = (EditText)findViewById(R.id.edit0);
        final EditText PE = (EditText)findViewById(R.id.edit1);
        final EditText ME = (EditText)findViewById(R.id.edit2);
        final EditText GE = (EditText)findViewById(R.id.edit3);
        final EditText GGE = (EditText)findViewById(R.id.edit4);

        final SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME,MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

        PPE.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String PP = PPE.getText().toString();

                method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/Atualizar_Carrinho_app";
                function = "carrinho";
                Model estTask = new Model();
                estTask.execute(function,method,PPIDS,PP,"item",user_id,hash);

                try {
                    String res = estTask.get();
                    JSONObject obj = new JSONObject(res.toString());
                    String disponivel = obj.getString("disponivel");
                    TextView PPD = (TextView)findViewById(R.id.Text0);
                    PPD.setText(disponivel);

                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso",Toast.LENGTH_SHORT);
                    toast.show();

            } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        PE.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String P = PE.getText().toString();

                method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/Atualizar_Carrinho_app";
                function = "carrinho";
                Model estTask = new Model();
                    estTask.execute(function,method, PIDS,P,"item",user_id,hash);

                try {
                    String res = estTask.get();
                    JSONObject obj = new JSONObject(res.toString());
                    String disponivel = obj.getString("disponivel");
                    TextView PD = (TextView)findViewById(R.id.Text1);
                    PD.setText(disponivel);
                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso",Toast.LENGTH_SHORT);
                    toast.show();

                }catch (JSONException e) {
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        ME.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String M = ME.getText().toString();

                method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/Atualizar_Carrinho_app";
                function = "carrinho";
                Model estTask = new Model();
                estTask.execute(function,method,MIDS,M,"item",user_id,hash);
                try {
                    String res = estTask.get();
                    JSONObject obj = new JSONObject(res.toString());
                    String disponivel = obj.getString("disponivel");
                    TextView MD = (TextView)findViewById(R.id.Text2);
                    MD.setText(disponivel);
                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso",Toast.LENGTH_SHORT);
                    toast.show();

                }catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }


        });

        GE.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                String G = GE.getText().toString();
                method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/Atualizar_Carrinho_app";
                function = "carrinho";
                Model estTask = new Model();
                estTask.execute(function,method, GIDS,G,"item",user_id,hash);

                try {
                    String res = estTask.get();
                    JSONObject obj = new JSONObject(res.toString());
                    String disponivel = obj.getString("disponivel");
                    TextView GD = (TextView)findViewById(R.id.Text3);
                    GD.setText(disponivel);
                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso",Toast.LENGTH_SHORT);
                    toast.show();

                }catch (JSONException e) {
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {

                }

            }
        });

        GGE.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                String GG = GGE.getText().toString();

                method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/Atualizar_Carrinho_app";
                function = "carrinho";
                Model estTask = new Model();
                estTask.execute(function,method, GGIDS,GG,"item",user_id,hash);

                try {
                    String res = estTask.get();
                    JSONObject obj = new JSONObject(res.toString());
                    String disponivel = obj.getString("disponivel");
                    TextView GGD = (TextView)findViewById(R.id.Text4);
                    GGD.setText(disponivel);
                    Toast toast = Toast.makeText(getApplicationContext(), "Pedido realizado com sucesso",Toast.LENGTH_SHORT);
                    toast.show();


                }catch (JSONException e) {
                    sharedPreferences.edit().clear().commit();
                    Intent intent = new Intent(ProdutoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        }} catch (InterruptedException e) {
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

    }
    public void estoque(String idEstoque, String methodS) throws ExecutionException, InterruptedException {

        function = "produto";
        Model estTask = new Model();
        estTask.execute(function,methodS, idEstoque);
        resul = estTask.get();
        try {

            JSONObject JASresultEs = new JSONObject(resul.toString());
            JSONObject JASresultTam = JASresultEs.getJSONObject("tamanho");
            JSONObject JASresultId = JASresultEs.getJSONObject("id");

            JSONArray PPS = JASresultTam.getJSONArray("PP");
            String PPSS = PPS.getString(0);
            TextView PP = (TextView)findViewById(R.id.Text0);
            PPID = JASresultId.getJSONArray("PP");
            PPIDS = PPID.getString(0);
            PP.setText(String.valueOf(PPSS));

            JSONArray PS = JASresultTam.getJSONArray("P");
            String PSS = PS.getString(0);
            TextView P = (TextView)findViewById(R.id.Text1);
            PID = JASresultId.getJSONArray("P");
            PIDS = PID.getString(0);
            P.setText(String.valueOf(PSS));

            JSONArray MS = JASresultTam.getJSONArray("M");
            String MSS = MS.getString(0);
            TextView M = (TextView)findViewById(R.id.Text2);
            MID = JASresultId.getJSONArray("M");
            MIDS = MID.getString(0);
            M.setText(String.valueOf(MSS));

            JSONArray GS = JASresultTam.getJSONArray("G");
            String GSS = GS.getString(0);
            TextView G = (TextView)findViewById(R.id.Text3);
            GID = JASresultId.getJSONArray("G");
            GIDS = GID.getString(0);
            G.setText(String.valueOf(GSS));

            JSONArray GGS = JASresultTam.getJSONArray("GG");
            String GGSS = GGS.getString(0);
            TextView GG = (TextView)findViewById(R.id.Text4);
            GGID = JASresultId.getJSONArray("GG");
            GGIDS = GGID.getString(0);
            GG.setText(String.valueOf(GGSS));

        } catch (JSONException e) {
            Log.e("tag",e.getMessage());
            e.printStackTrace();
        }
    }
    public void carrinho(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(ProdutoActivity.this, CheckoutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
    }

    public void produtos(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(ProdutoActivity.this, GridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_right_leave,R.anim.anim_slide_left_leave);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProdutoActivity.this, GridActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_right_leave,R.anim.anim_slide_left_leave);
        finish();
    }

}