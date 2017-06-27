package com.uniquesys.qrgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ProdutoActivity extends AppCompatActivity {

    String id;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();

        String resultado = bundle.getString("resultado");
        ImageView im = (ImageView) findViewById(R.id.ImgProd);
        try {
            JSONArray JASresult = new JSONArray(resultado.toString());
            JSONObject obj = JASresult.getJSONObject(0);
            String nome = obj.getString("prod_text");
            String preco = obj.getString("prod_preco");
            String ref = obj.getString("prod_ref");
            String img = obj.getString("img_nome");
            id = obj.getString("prod_id");
            TextView txtProd = (TextView)findViewById(R.id.txtProd);
            txtProd.setText(nome);
            TextView txtPreco = (TextView)findViewById(R.id.txtPreco);
            txtPreco.setText(preco);
            TextView txtRef = (TextView)findViewById(R.id.txtReferencia);
            txtRef.setText(ref);

            String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
            String method = urlOfImage;
            String function = "imagem";
            Imagem imgTask = new Imagem();
            imgTask.execute(function,method);

            Bitmap result = null;
            try {

                result  = imgTask.get();
                im.setImageBitmap(result);
                image = result;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String codigo = id;
        String method = "https://www.uniquesys.com.br/qrgo/produtos/prod_app";
        String function = "produto";
        Model prodTask = new Model(this);
        prodTask.execute(function,method, codigo);
        String resul = null;
        try {
            resul = prodTask.get();
            JSONArray JASresultProd = new JSONArray(resul.toString());
            Log.e("Imagem", String.valueOf(JASresultProd));
            for(int i=0; i < JASresultProd.length();i++){
                JSONObject obj =  JASresultProd.getJSONObject(i);
                String img = obj.getString("img_nome");
                if(img != "null"){
                String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
                method = urlOfImage;
                function = "imagem";
                Imagem imgTask = new Imagem();
                imgTask.execute(function,method);

                Bitmap result = null;
                try {
                    result  = imgTask.get();
                    ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(result);
                    simpleViewFlipper.addView(imageView);
                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper)findViewById(R.id. relativeLayout4);
                    ImageView imageViewCollor = new ImageView(this);
                    simpleViewFlipperCollor.addView(imageViewCollor);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else{
                    ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(image);
                    simpleViewFlipper.addView(imageView);
                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper)findViewById(R.id. relativeLayout4);
                    ImageView imageViewCollor = new ImageView(this);
                    String color = obj.getString("prod_color");
                    imageViewCollor.setBackgroundColor(Color.parseColor(color));
                    simpleViewFlipperCollor.addView(imageViewCollor);

                    ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id. relativeLayout4);
                    TextView TextViewNome = new TextView(this);
                    String cor_nome = obj.getString("cor_nome");
                    TextViewNome.setText(cor_nome);
                    simpleViewFlipperNome.addView(TextViewNome);

                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
            Log.e("Imagem", "Erro");
        } catch (ExecutionException e) {
            e.printStackTrace();
            Log.e("Imagem", "Erro");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("Imagem", "Erro");
        }
    }

    public void grid(View v) throws ExecutionException, InterruptedException, JSONException {
        Intent intent = new Intent(ProdutoActivity.this, GridActivity.class);
        startActivity(intent);

    }

    public void  next(View v){
        ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showNext();
        ViewFlipper simpleViewFlipperColor=(ViewFlipper)findViewById(R.id. relativeLayout4);
        simpleViewFlipperColor.showNext();
        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id. relativeLayout4);
        simpleViewFlipperNome.showNext();
    }
    public void Previous (View v){
        ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showPrevious();
        ViewFlipper simpleViewFlipperColor=(ViewFlipper)findViewById(R.id. relativeLayout4);
        simpleViewFlipperColor.showPrevious();
        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id. relativeLayout4);
        simpleViewFlipperNome.showPrevious();
    }

}
