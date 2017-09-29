package com.uniquesys.qrgo.Produtos.CheckOut;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.model.Imagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CheckOutActivity extends AppCompatActivity {

    String user_id;
    String hash;
    private static final String PREF_NAME = "USER_LOG";
    String splittedData;
    List<Bitmap> splittedBitmaps = new ArrayList<>();
    ProgressDialog pd;
    String resultado;
    JSONArray JASresult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash", "");

        this.getGridImage();
    }

    public void getGridImage(){


        new Thread()
        {

            public void run() {
                Model prodTask = new Model();
                final String method = "http://192.168.0.85/erp/vendas_pedidos/get";
                final String function = "listagem";
                prodTask.execute(function, method, String.valueOf(0), user_id,hash);

                try {


                    resultado = prodTask.get();
                    splittedData = resultado;
                    JASresult = new JSONArray(resultado.toString());

                    try {
                        for (int i = 0; i < JASresult.length(); i++) {
                            JSONObject obj = JASresult.getJSONObject(i);
                            String img = obj.getString("image_thumb");
                            try {

                                if (!img.equals("null")) {
                                    String urlOfImage = "http://192.168.0.85/erp/uploads/profiles/" + img;
                                    String method2 = urlOfImage;
                                    String function2 = "imagem";
                                    Imagem imgTask = new Imagem();
                                    imgTask.execute(function2, method2);
                                    Bitmap result = imgTask.get();

                                    splittedBitmaps.add(result);

                                }

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            String img_test = obj.getString("image_thumb");
                        }
                    } catch (Exception e) {
                        Log.e("tag", e.getMessage());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                this.RefreshGrid(splittedData,splittedBitmaps);
            }

            private void RefreshGrid(String splittedData, List<Bitmap> splittedBitmaps) {

                    Fragment fragment = new CheckOutFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putString("data",splittedData);
                    bundle.putString("user_id",user_id);
                    bundle.putString("hash",hash);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentCheckOut, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }

        }.start();

    }
    }

