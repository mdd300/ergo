package com.uniquesys.qrgo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class ProdutoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto);
        Intent intent = getIntent();
        final ImageView iv = (ImageView) findViewById(R.id.ImgProd);

        Bundle bundle = intent.getExtras();

        String resultado = bundle.getString("resultado");
        try {
            JSONArray JASresult = new JSONArray(resultado.toString());
            JSONObject obj = JASresult.getJSONObject(0);
            String nome = obj.getString("prod_text");
            String preco = obj.getString("prod_preco");
            String ref = obj.getString("prod_ref");
            String img = obj.getString("img_nome");
            TextView txtProd = (TextView)findViewById(R.id.txtProd);
            txtProd.setText(nome);
            TextView txtPreco = (TextView)findViewById(R.id.txtPreco);
            txtPreco.setText(preco);
            TextView txtRef = (TextView)findViewById(R.id.txtReferencia);
            txtRef.setText(ref);

            String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
            String method = urlOfImage;
            ImageView im = (ImageView) findViewById(R.id.ImgProd);
            String function = "imagem";
            Imagem imgTask = new Imagem();
            imgTask.execute(function,method);

            Bitmap result = null;
            try {
                result  = imgTask.get();
                im.setImageBitmap(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
