package com.uniquesys.qrgo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridActivity extends AppCompatActivity {

    List<Bitmap> splittedBitmaps = new ArrayList<>();
    List<String> splittedid = new ArrayList<>();
    List<Bitmap> splittedBitmapsPes = new ArrayList<>();
    List<String> splittedidPes = new ArrayList<>();
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
        EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
        ImageView btnPesquisa = (ImageView) findViewById(R.id.buttonPesquisa);
        CampoPesquisa.setVisibility(View.INVISIBLE);
        btnPesquisa.setVisibility(View.INVISIBLE);

        Button button = (Button) findViewById(R.id.TextLoad);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle the click here
                try {
                    Pagination(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

public void getGridImage(){


    new Thread()
    {

        public void run()
        {
            Model prodTask = new Model();
            final String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/grid_listagem";
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
                            String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/uploads/produtos/img/" + img;
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
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            Fragment fragment = null;

            if (width >= 720) {
                fragment = new ResFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }

            else
            {
                fragment =  new GridFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

            }

        }

    }.start();

}

    public void Pagination(View v) throws ExecutionException, InterruptedException {
        pd = ProgressDialog.show(GridActivity.this, "Carregando", "Aguarde...", true, false);
        pagina++;
        this.getGridImage();

    }

    public void carrinho(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(GridActivity.this,CheckoutActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();

    }
    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }

    public void pesquisaInicias(View v) throws ExecutionException, InterruptedException {
        EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
        ImageView btnPesquisa = (ImageView) findViewById(R.id.buttonPesquisa);
        ImageView btnAbrirPesquisa = (ImageView) findViewById(R.id.imageButton7);
        TextView prod = (TextView) findViewById(R.id.textView4);

        CampoPesquisa.setVisibility(View.VISIBLE);
        btnPesquisa.setVisibility(View.VISIBLE);
        btnAbrirPesquisa.setVisibility(View.INVISIBLE);
        prod.setVisibility(View.INVISIBLE);

        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(CampoPesquisa, InputMethodManager.SHOW_IMPLICIT);

    }

    public void pesquisa(View v) throws ExecutionException, InterruptedException {
        pagina = 1;
        splittedBitmaps.clear();
        splittedid.clear();
        this.pesquisaProd();
    }

    public void pesquisaProd(){

        Button button = (Button) findViewById(R.id.TextLoad);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle the click here
                try {
                    PaginationPesquisa(v);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        View frag = findViewById(R.id.fragment_frame);
        frag.setVisibility(View.INVISIBLE);

        pd = ProgressDialog.show(GridActivity.this, "Carregando", "Aguarde...", true, false);

        new Thread()
        {

            public void run() {

                EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
                String StringPesquisar = CampoPesquisa.getText().toString();

                Model prodTask = new Model();
                String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/grid_listagem_pesquisa";
                String function = "pesquisa";
                prodTask.execute(function, method, String.valueOf(pagina), StringPesquisar);

                try {


                    resultado = prodTask.get();
                    JASresult = new JSONArray(resultado.toString());
                    try {
                        for (int i = 0; i < JASresult.length(); i++) {
                            JSONObject obj = JASresult.getJSONObject(i);
                            String img = obj.getString("img_nome");
                            String id = obj.getString("prod_uniq");

                            try {

                                if (!img.equals("null")) {
                                    String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/uploads/produtos/img/" + img;
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

                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                Fragment fragment = null;
                if (width >= 720) {

                    Fragment fr = new ResPesquisaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                    fr.setArguments(bundle);
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.pesquisas, fr);
                    fragmentTransaction.commit();
                }

                else
                {
                    Fragment fr = new PesquisaFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                    fr.setArguments(bundle);
                    android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.pesquisas, fr);
                    fragmentTransaction.commit();

                }

                pd.dismiss();
            }
        }.start();


    }

    public void PaginationPesquisa(View v) throws ExecutionException, InterruptedException {
        pagina++;
        this.pesquisaProd();
    }
    @Override
    public void onPause(){
        super.onPause();

    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

    }
    @Override
    public void onStop(){
        super.onPause();

    }

}
