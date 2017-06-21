package com.uniquesys.qrgo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridActivity extends AppCompatActivity implements  GridFragment.OnFragmentInteractionListener{

     private Bitmap bitmap;
    private ImageView image;
    Model prodTask = new Model(this);


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        String method = "https://www.uniquesys.com.br/qrgo/pedidos/grid_listagem";
        String function = "listagem";
        prodTask.execute(function, method);
        String resultado = null;
        Bitmap result = null;
        String img_test = null;
        List<Bitmap> splittedBitmaps = new ArrayList<>();
        List<String> splittedid = new ArrayList<>();


        try {

            resultado = prodTask.get();
            JSONArray JASresult = new JSONArray(resultado.toString());
            for (int i = 0; i < 12; i++) {
                JSONObject obj = JASresult.getJSONObject(i);
                String img = obj.getString("img_nome");
                String id = obj.getString("prod_id");


                try {



                        if (!img.equals("null")) {
                            String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
                            method = urlOfImage;
                            function = "imagem";
                            Imagem imgTask = new Imagem();
                            imgTask.execute(function, method);
                            result = imgTask.get();
                            splittedid.add(id);

                            splittedBitmaps.add(result);

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

        Fragment fragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
        bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();



    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void itemClicked(Context context,String id) {
        Log.e("Imagem", id);
        String codigo = id;
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

        Intent intent = new Intent(context, ProdutoActivity.class);

        Bundle bundle = new Bundle();
        Log.e("Imagem","Teste");
        bundle.putString("resultado", resultado.toString());
        intent.putExtras(bundle);
        startActivity(intent);

    }
}
