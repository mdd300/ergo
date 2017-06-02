package com.uniquesys.qrgo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        String method = "https://www.uniquesys.com.br/qrgo/pedidos/grid_listagem";
        String function = "listagem";
        Model prodTask = new Model(this);
        prodTask.execute(function,method);
        String resultado = null;
        GridView gridView = (GridView) findViewById(R.id.gridProdutos);
        Bitmap result = null;
        String img_test = null;
        try {

            resultado = prodTask.get();
            JSONArray JASresult = new JSONArray(resultado.toString());
            for(int i = 0; i < JASresult.length(); i++){
            JSONObject obj = JASresult.getJSONObject(i);
            String img = obj.getString("img_nome");

            try {

                if(!img.equals(img_test)) {
                    Log.e("Imagem", img);
                    if (!img.equals("null") && !img.equals("58c5ef25001c4.jpeg")) {
                        String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
                        method = urlOfImage;
                        function = "imagem";
                        Imagem imgTask = new Imagem(this);
                        imgTask.execute(function, method);
                        result = imgTask.get();
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
                img_test = obj.getString("img_nome");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_grid);



    }
}
