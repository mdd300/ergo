package com.uniquesys.qrgo.Produtos.ListProdutos;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PesquisaImageListProd extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    private int resource;
    private LayoutInflater inflater;
    String resultado;

    public PesquisaImageListProd(Context c, List<Bitmap> splittedBitmaps, List<String> splittedid) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;
        resource = R.layout.listview_fragment_layout;
        inflater = LayoutInflater.from(mContext);
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
        final RelativeLayout convertViewR = (RelativeLayout) inflater.inflate(resource, null);

        String codigo = id.get(position);
        String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/readqrcodepedido_app";
        String function = "produto";
        Model prodTask = new Model(mContext);
        prodTask.execute(function,method, codigo);
        try {
            final String resultado = prodTask.get();
            JSONArray JASresult = new JSONArray(resultado.toString());
            JSONObject obj = JASresult.getJSONObject(0);
            String nome = obj.getString("prod_desc");
            String preco = obj.getString("prod_preco");
            String ref = obj.getString("prod_ref");
            TextView Preco = (TextView) convertViewR.findViewById(R.id.txtPreco);
            TextView Ref = (TextView) convertViewR.findViewById(R.id.txtRef);
            TextView Nome = (TextView) convertViewR.findViewById(R.id.nome);
            TextView Prazo = (TextView) convertViewR.findViewById(R.id.ult_mes);

            Nome.setText(nome);
            Preco.setText(preco);
            Ref.setText(ref);

            ImageView imageview = (ImageView) convertViewR.findViewById(R.id.complet_image);

            imageview.setImageBitmap(data.get(position));

            convertViewR.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String codigo = id.get(position);
                    Intent in = new Intent(mContext, ProdutoActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    in.putExtra("id",codigo);
                    in.putExtra("resultado", resultado);
                    mContext.startActivity(in);
                    ((ListViewActivity) mContext).overridePendingTransition(R.anim.anim_slide_right,R.anim.anim_slide_left);
                    ((ListViewActivity) mContext).finish();

                }
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertViewR;
    }


}