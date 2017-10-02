package com.uniquesys.qrgo.Produtos.ListProdutos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uniquesys.qrgo.Chat.ChatActivity;
import com.uniquesys.qrgo.Clientes.ClientesActivity;
import com.uniquesys.qrgo.MainActivity;
import com.uniquesys.qrgo.Produtos.CheckOut.CheckOutActivity;
import com.uniquesys.qrgo.Produtos.CheckoutActivity;
import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.config.ConfiguracaoFirebase;
import com.uniquesys.qrgo.model.Imagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListViewActivity extends AppCompatActivity {

    List<Bitmap> splittedBitmaps = new ArrayList<>();
    List<String> splittedid = new ArrayList<>();
    List<String> Contatos = new ArrayList<>();
    String ResProd ;
    JSONArray JASresult;
    JSONArray JASRes;
    String resultado = null;
    Bitmap result = null;
    String img_test = null;
    int pagina = 1;
    ProgressDialog pd;
    String method2;
    String function2;
    String hash;
    private static final String PREF_NAME = "USER_LOG";
    String user_id;
    DatabaseReference firebaseLast;
    ValueEventListener valueEventListenerLastMensagemNot;
    int j = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        user_id = sharedPreferences.getString("user_id", "");
        hash = sharedPreferences.getString("hash","");
        EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
        ImageView btnPesquisa = (ImageView) findViewById(R.id.buttonPesquisa);
        ImageView x = (ImageView) findViewById(R.id.x);
        CampoPesquisa.setVisibility(View.INVISIBLE);
        btnPesquisa.setVisibility(View.INVISIBLE);
        x.setVisibility(View.INVISIBLE);

        CampoPesquisa.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            pesquisa();

                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        valueEventListenerLastMensagemNot = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

        if (j > 0) {
            NotificationManager nm = (NotificationManager) ListViewActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent p = PendingIntent.getActivity(ListViewActivity.this, 0, new Intent(ListViewActivity.this, ChatActivity.class), 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(ListViewActivity.this);

            builder.setTicker("Nova Mensagem");
            builder.setContentTitle("QRGO");
            builder.setContentText("Nova Mensagem");
            builder.setSmallIcon(R.drawable.logo);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
            builder.setContentIntent(p);

            Notification n = builder.build();
            n.vibrate = new long[]{150, 300, 150, 600};
            nm.notify(R.drawable.logo, n);

            try {
                Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone toque = RingtoneManager.getRingtone(ListViewActivity.this, som);
                toque.play();
            } catch (Exception e) {

            }

        }
        j++;
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

};

        firebaseLast = ConfiguracaoFirebase.getFirebase().child(user_id).child("Contatos");

                firebaseLast.addValueEventListener(valueEventListenerLastMensagemNot);


        if (savedInstanceState == null) {

            pd = ProgressDialog.show(ListViewActivity.this, "Carregando", "Aguarde...", true, false);
            this.getGridImage();

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

        }else{

            splittedBitmaps = savedInstanceState.getParcelableArrayList("bitmap");
            splittedid = savedInstanceState.getStringArrayList("id");
            this.RefreshGrid(splittedid,splittedBitmaps);
        }
    }
    public void getGridImage(){


        new Thread()
        {

            public void run() {
                Model prodTask = new Model();
                final String method = "http://192.168.0.85/erp/vendas_produtos/getList";
                final String function = "listagem";
                prodTask.execute(function, method, String.valueOf(pagina), user_id, hash);

                try {
                    if (resultado == null) {
                        resultado = prodTask.get();
                        JASresult = new JSONArray(resultado.toString());
                        JASRes = new JSONArray(resultado.toString());
                        ResProd = resultado;
                    }else{
                        resultado = prodTask.get();
                        JSONArray JASresultR = new JSONArray(resultado.toString());
                        for (int i = 0; i < JASresultR.length(); i++) {
                            JSONObject obj = JASresultR.getJSONObject(i);
                            JASRes.put(obj);
                        }
                        ResProd = JASRes.toString();
                        Log.e("teste",ResProd);
                        JASresult = new JSONArray(resultado.toString());
                    }
                    try {
                        for (int i = 0; i < JASresult.length(); i++) {
                            JSONObject obj = JASresult.getJSONObject(i);

                            String img = obj.getString("image_thumb");
                            String id = obj.getString("prod_id");
                            try {

                                if (!img.equals("null")) {
                                    String urlOfImage = "http://192.168.0.85/erp/uploads/profiles/" + img;
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
                            img_test = obj.getString("image_thumb");
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
                    fragment = new ResListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                    bundle.putStringArrayList("Contatos", (ArrayList<String>) Contatos);
                    bundle.putString("user", (user_id));
                    bundle.putString("hash",hash);
                    bundle.putString("resultado", ResProd);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_ListProd, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                }

                else
                {
                    fragment =  new ListProdFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                    bundle.putStringArrayList("Contatos", (ArrayList<String>) Contatos);
                    bundle.putString("user", (user_id));
                    bundle.putString("hash",hash);
                    bundle.putString("resultado", ResProd);
                    fragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_ListProd, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

                }

            }

        }.start();

    }

    private void RefreshGrid(List<String> splittedid, List<Bitmap> splittedBitmaps) {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        Fragment fragment = null;

        if (width >= 720) {
            fragment = new ResListFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
            bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
            bundle.putStringArrayList("Contatos", (ArrayList<String>) Contatos);
            bundle.putString("user", (user_id));
            bundle.putString("hash",hash);
            bundle.putString("resultado", ResProd);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_ListProd, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        }

        else
        {
            fragment =  new ListProdFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
            bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
            bundle.putStringArrayList("Contatos", (ArrayList<String>) Contatos);
            bundle.putString("user", (user_id));
            bundle.putString("hash",hash);
            bundle.putString("resultado", ResProd);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_ListProd, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();

        }

    }

    public void Pagination(View v) throws ExecutionException, InterruptedException {
        pd = ProgressDialog.show(ListViewActivity.this, "Carregando", "Aguarde...", true, false);
        pagina++;
        this.getGridImage();

    }


    public void pesquisaInicias(View v) throws ExecutionException, InterruptedException {
        EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
        ImageView btnPesquisa = (ImageView) findViewById(R.id.buttonPesquisa);
        ImageView btnAbrirPesquisa = (ImageView) findViewById(R.id.imageButton7);
        ImageView btn = (ImageView) findViewById(R.id.imageButton6);
        ImageView btngrid = (ImageView) findViewById(R.id.imageButton2);
        ImageView x = (ImageView) findViewById(R.id.x);
        TextView prod = (TextView) findViewById(R.id.textView4);

        CampoPesquisa.setVisibility(View.VISIBLE);
        btnPesquisa.setVisibility(View.VISIBLE);
        x.setVisibility(View.VISIBLE);
        btnAbrirPesquisa.setVisibility(View.INVISIBLE);
        btngrid.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
        prod.setVisibility(View.INVISIBLE);

        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(CampoPesquisa, InputMethodManager.SHOW_IMPLICIT);

    }
    public void pesquisaTerminar(View v) throws ExecutionException, InterruptedException {
        EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
        ImageView btnPesquisa = (ImageView) findViewById(R.id.buttonPesquisa);
        ImageView btnAbrirPesquisa = (ImageView) findViewById(R.id.imageButton7);
        ImageView btn = (ImageView) findViewById(R.id.imageButton6);
        ImageView btngrid = (ImageView) findViewById(R.id.imageButton2);
        ImageView x = (ImageView) findViewById(R.id.x);
        TextView prod = (TextView) findViewById(R.id.textView4);

        CampoPesquisa.setVisibility(View.INVISIBLE);
        btnPesquisa.setVisibility(View.INVISIBLE);
        x.setVisibility(View.INVISIBLE);
        btnAbrirPesquisa.setVisibility(View.VISIBLE);
        btngrid.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        prod.setVisibility(View.VISIBLE);

        InputMethodManager imm=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(CampoPesquisa, InputMethodManager.SHOW_IMPLICIT);

    }
    public void pesquisa(View v) throws ExecutionException, InterruptedException {
        pagina = 1;
        splittedBitmaps.clear();
        splittedid.clear();
        this.pesquisaProd();
    }

    public void pesquisa() {
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

        View frag = findViewById(R.id.fragment_ListProd);
        frag.setVisibility(View.INVISIBLE);

        pd = ProgressDialog.show(ListViewActivity.this, "Carregando", "Aguarde...", true, false);

        new Thread()
        {

            public void run() {

                EditText CampoPesquisa = (EditText) findViewById(R.id.editTextPesquisa);
                String StringPesquisar = CampoPesquisa.getText().toString();

                Model prodTask = new Model();
                String method = "http://192.168.0.85/erp/vendas_produtos/getList";
                String function = "pesquisa";
                prodTask.execute(function, method, String.valueOf(pagina), StringPesquisar,user_id,hash);

                try {


                    resultado = prodTask.get();
                    ResProd = resultado;
                    JASresult = new JSONArray(resultado.toString());
                    try {
                        for (int i = 0; i < JASresult.length(); i++) {
                            JSONObject obj = JASresult.getJSONObject(i);
                            String img = obj.getString("image_thumb");
                            String id = obj.getString("prod_id");

                            try {

                                if (!img.equals("null")) {
                                    String urlOfImage = "http://192.168.0.85/erp/uploads/profiles/" + img;
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
                            img_test = obj.getString("image_thumb");
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

                    Fragment fr = new ResPesquisaListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                    bundle.putString("user", (user_id));
                    bundle.putString("hash",hash);
                    bundle.putStringArrayList("Contatos", (ArrayList<String>) Contatos);
                    bundle.putString("resultado", ResProd);
                    fr.setArguments(bundle);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.pesquisas_ListProd, fr);
                    fragmentTransaction.commit();
                }

                else
                {
                    Fragment fr = new PesquisaListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("lista", (ArrayList<? extends Parcelable>) splittedBitmaps);
                    bundle.putStringArrayList("id", (ArrayList<String>) splittedid);
                    bundle.putString("user", (user_id));
                    bundle.putString("hash",hash);
                    bundle.putStringArrayList("Contatos", (ArrayList<String>) Contatos);
                    bundle.putString("resultado", ResProd);
                    fr.setArguments(bundle);
                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.pesquisas_ListProd, fr);
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


    public void carrinho(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ListViewActivity.this,CheckOutActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    public void chat(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ListViewActivity.this,ChatActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_up_leave,R.anim.anim_slide_down_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);

    }
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putParcelableArrayList("bitmap", (ArrayList<? extends Parcelable>) splittedBitmaps);
        savedInstanceState.putStringArrayList("id", (ArrayList<String>) splittedid);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    public void Logout(View v) throws ExecutionException, InterruptedException {
        SharedPreferences.Editor userPref = getSharedPreferences(PREF_NAME,MODE_PRIVATE).edit();
        userPref.clear();
        Intent intent = new Intent(ListViewActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
    }

    public void Grid(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ListViewActivity.this,GridActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_fade_in, R.anim.anim_fade_in);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }
    public void Clientes(View v) throws ExecutionException, InterruptedException {
        Intent intent_next=new Intent(ListViewActivity.this,ClientesActivity.class);
        startActivity(intent_next);
        overridePendingTransition(R.anim.anim_slide_right_leave, R.anim.anim_slide_left_leave);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }
    public void camera(View v) throws ExecutionException, InterruptedException {
        Intent intent = new Intent(ListViewActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_up,R.anim.anim_slide_down);
        finish();
        firebaseLast.removeEventListener(valueEventListenerLastMensagemNot);
    }


}
