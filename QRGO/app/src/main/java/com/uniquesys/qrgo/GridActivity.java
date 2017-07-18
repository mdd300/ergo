package com.uniquesys.qrgo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridActivity extends AppCompatActivity {

    List<Bitmap> splittedBitmaps = new ArrayList<>();
    List<String> splittedid = new ArrayList<>();
    JSONArray JASresult;
    String resultado = null;
    Bitmap result = null;
    String img_test = null;
    int pagina = 1;
    ProgressDialog pd;
    String method2;
    String function2;
    private static final String PREF_NAME = "USER_LOG";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        pd = ProgressDialog.show(GridActivity.this, "Carregando", "Aguarde...", true, false);
        this.getGridImage();


    }

public void getGridImage(){


    new Thread()
    {

        public void run()
        {
            Model prodTask = new Model();
            final String method = "https://www.uniquesys.com.br/qrgo/pedidos/grid_listagem";
            final String function = "listagem";
            prodTask.execute(function, method, String.valueOf(pagina));

            try {


                resultado = prodTask.get();
                JASresult = new JSONArray(resultado.toString());
            try
            {
                for (int i = 0; i < JASresult.length(); i++) {
                    JSONObject obj = JASresult.getJSONObject(i);
                    String img = obj.getString("img_nome");
                    String id = obj.getString("prod_uniq");

                    try {

                        if (!img.equals("null")) {
                            String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
                            method2 = urlOfImage;
                            function2 = "imagem";
                            Imagem imgTask = new Imagem();
                            imgTask.execute(function2, method2);
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
            }
            catch (Exception e)
            {
                Log.e("tag",e.getMessage());
            }} catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            this.RefreshGrid(splittedid,splittedBitmaps);
            pd.dismiss();
        }

        private void RefreshGrid(List<String> splittedid, List<Bitmap> splittedBitmaps) {
            Fragment fragment = new GridFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
            bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();


        }


    }.start();



}


    public void Pagination(View v) throws ExecutionException, InterruptedException {
        pd = ProgressDialog.show(GridActivity.this, "Carregando", "Aguarde...", true, false);
        pagina++;
        this.getGridImage();


    }

    public void carrinho(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(GridActivity.this, CheckoutActivity.class);
        startActivity(intent);

    }



}
