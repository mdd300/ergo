package com.uniquesys.qrgo;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GridActivity extends Fragment {

     private Bitmap bitmap;
    private ImageView image;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.activity_grid, container,
                false);


        String method = "https://www.uniquesys.com.br/qrgo/pedidos/grid_listagem";
        String function = "listagem";
        Model prodTask = new Model();
        prodTask.execute(function, method);
        String resultado = null;
        Bitmap result = null;
        String img_test = null;
        List<Bitmap> splittedBitmaps = new ArrayList<>();
        ;


        try {

            resultado = prodTask.get();
            JSONArray JASresult = new JSONArray(resultado.toString());
            for (int i = 0; i < 10; i++) {
                JSONObject obj = JASresult.getJSONObject(i);
                String img = obj.getString("img_nome");


                try {

                    if (!img.equals(img_test)) {
                        Log.e("Imagem", img);
                        if (!img.equals("null") && !img.equals("58c5ef25001c4.jpeg")) {
                            String urlOfImage = "https://www.uniquesys.com.br/qrgo/uploads/produtos/img/" + img;
                            method = urlOfImage;
                            function = "imagem";
                            Imagem imgTask = new Imagem();
                            imgTask.execute(function, method);
                            result = imgTask.get();

                            splittedBitmaps.add(result);

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

        View myView = inflater.inflate(R.layout.activity_grid, null);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridProdutos);
        SplittedImageAdapter adapter = new SplittedImageAdapter(getActivity(), splittedBitmaps);


        gridView.setAdapter(adapter);

        return myView;
    }

}
