package com.uniquesys.qrgo.Produtos.CheckOut;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.Enviar_mensagem_prod;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.model.Imagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplittedModalVariantes extends BaseAdapter {

    Context mContext;
    String data;
    ArrayList<Bitmap> splittedVar = new ArrayList<>();
    private int resource;
    private LayoutInflater inflater;
    JSONObject obj;
    int i;

    public SplittedModalVariantes(Context c, ArrayList<Bitmap> splittedBit, String dataVar, int j) {

        mContext = c;
        data = dataVar;
        splittedVar = splittedBit;
        resource = R.layout.layout_checkout_produtos;
        inflater = LayoutInflater.from(mContext);
        i = j;
    }

    @Override
    public int getCount() {

        return splittedVar.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup arg2) {

        RelativeLayout convertViewR = (RelativeLayout) inflater.inflate(resource, null);

        JSONArray JASresult = null;

        try {
            JASresult = new JSONArray(data.toString());
            obj = JASresult.getJSONObject(i);
            String nome = obj.getString("prod_desc");
            String preco = obj.getString("prod_preco");
            String quantidade = obj.getString("carrinho_qtde");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertViewR;
    }


}