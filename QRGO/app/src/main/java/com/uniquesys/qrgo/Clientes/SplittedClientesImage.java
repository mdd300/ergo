package com.uniquesys.qrgo.Clientes;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplittedClientesImage extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    private LayoutInflater inflater;
    private int resource;
    List<String> DadosCliente;

    public SplittedClientesImage(Context c, List<Bitmap> splittedBitmaps, List<String> splittedid, ArrayList<String> dados) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;
        resource = R.layout.listview_fragment_clientes;
        inflater = LayoutInflater.from(mContext);
        DadosCliente = dados;
    }

    @Override
    public int getCount() {

        return data.size();
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

        ImageView imageView;
        if (convertView == null) {

            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(180, 180));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(1,2,1,2);


        }
        final RelativeLayout convertViewR = (RelativeLayout) inflater.inflate(resource, null);
        ImageView imageview = (ImageView) convertViewR.findViewById(R.id.complet_image);

        imageview.setImageBitmap(data.get(position));

        TextView razao = (TextView) convertViewR.findViewById(R.id.nome);
        TextView fantasia = (TextView) convertViewR.findViewById(R.id.txtRef);
        TextView cnpj = (TextView) convertViewR.findViewById(R.id.ult_mes);
        TextView cidade = (TextView) convertViewR.findViewById(R.id.txtPreco);

        try {
            Log.e("cliente",DadosCliente.get(position).toString());
            JSONArray JASresult = new JSONArray(DadosCliente.get(position).toString());
            JSONObject obj = JASresult.getJSONObject(0);
            razao.setText( obj.getString("cliente_razao"));
            fantasia.setText( obj.getString("cliente_fantasia"));
            cnpj.setText( obj.getString("cliente_cnpj"));
            cidade.setText( obj.getString("cliente_cidade"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertViewR;
    }


}