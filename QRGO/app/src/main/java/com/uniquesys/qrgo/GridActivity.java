package com.uniquesys.qrgo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

public class GridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        String method = "https://www.uniquesys.com.br/qrgo/pedidos/grid_listagem";
        String function = "produto";
        Model prodTask = new Model(this);
        prodTask.execute(function,method);
        String resultado = null;
        try {
            resultado = prodTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
