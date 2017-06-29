package com.uniquesys.qrgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
    Context ctx;


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
                    onSwipeRight();
                else
                    onSwipeLeft();
                return true;
            }
            return false;
        }
    }

    public void onSwipeRight() {
        ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showNext();

        ViewFlipper simpleViewFlipperColor=(ViewFlipper)findViewById(R.id.relativeLayout4);
        simpleViewFlipperColor.showNext();

        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
        simpleViewFlipperNome.showNext();
    }

    public void onSwipeLeft() {
        ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
        simpleViewFlipper.showPrevious();

        ViewFlipper simpleViewFlipperColor=(ViewFlipper)findViewById(R.id.relativeLayout4);
        simpleViewFlipperColor.showPrevious();

        ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
        simpleViewFlipperNome.showPrevious();
    }


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
            ViewFlipper simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
            simpleViewFlipper.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });


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

                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(result);
                    simpleViewFlipper.addView(imageView);

                    ViewFlipper simpleViewFlipperCollor=(ViewFlipper)findViewById(R.id.relativeLayout4);
                    ImageView imageViewCollor = new ImageView(this);
                    simpleViewFlipperCollor.addView(imageViewCollor);

                    ViewFlipper simpleViewFlipperNome=(ViewFlipper)findViewById(R.id.relativeLayout5);
                    TextView TextViewNome = new TextView(this);
                    simpleViewFlipperNome.addView(TextViewNome);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            else{
                    simpleViewFlipper=(ViewFlipper)findViewById(R.id. relativeLayout3);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(image);
                    simpleViewFlipper.addView(imageView);

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

                }
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

    }


}
