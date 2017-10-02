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
import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
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

public class SplittedListCheckOut extends BaseAdapter {

    Context mContext;
    String resultado;
    List<Bitmap> data;
    ArrayList<Bitmap> splittedVar = new ArrayList<>();
    String user_id;
    String user_hash;
    JSONObject obj;
    private int resource;
    private LayoutInflater inflater;
    String id;
    String VarData;

    public SplittedListCheckOut(Context c, ArrayList<Bitmap> splittedBit, String splittedid, String user, String hash) {

        mContext = c;
        resultado = splittedid;
        data = splittedBit;
        user_hash = hash;
        user_id = user;
        resource = R.layout.layout_checkout_produtos;
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

        splittedVar.clear();

        RelativeLayout convertViewR = (RelativeLayout) inflater.inflate(resource, null);

        final TextView textNome = (TextView) convertViewR.findViewById(R.id.textNome);
        TextView textPreco = (TextView) convertViewR.findViewById(R.id.textBase);
        TextView textQuantidade = (TextView) convertViewR.findViewById(R.id.textQuantidade);
        TextView textTotal = (TextView) convertViewR.findViewById(R.id.textTotal);

        JSONArray JASresult = null;
        try {
            JASresult = new JSONArray(resultado.toString());
            obj = JASresult.getJSONObject(position);
            String nome = obj.getString("prod_desc");
            String preco = obj.getString("prod_preco");
            String quantidade = obj.getString("carrinho_qtde");
            id = obj.getString("prod_id");

            int valorTotal =  Integer.parseInt(preco) * Integer.parseInt(quantidade);

            textNome.setText(nome);
            textPreco.setText(preco);
            textQuantidade.setText(quantidade);
            textTotal.setText(String.valueOf(valorTotal));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageView imageview = (ImageView) convertViewR.findViewById(R.id.imageProdPrincipal);

        imageview.setImageBitmap(data.get(position));

        Model prodTask = new Model();
        final String method = "http://192.168.0.85/erp/vendas_pedidos/variantes";
        final String function = "produto";
        prodTask.execute(function, method,id, user_id,user_hash,"pa.prod_id_fk");

        try {

            final String resultVar = prodTask.get();
            VarData = resultVar;
            JASresult = new JSONArray(resultVar.toString());

            try {
                final ArrayList<Bitmap> splitted = new ArrayList<>();
                final String var = VarData;
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

                            splittedVar.add(result);
                            splitted.add(result);
                        }

                        final int finalI = i;
                        convertViewR.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle("Variantes");

                                ListView modeList = new ListView(mContext);
                                SplittedModalVariantes modal = new SplittedModalVariantes(mContext,splitted,var, finalI);
                                modeList.setAdapter(modal);

                                builder.setView(modeList);
                                final Dialog dialog = builder.create();

                                dialog.show();

                            }
                        });

                        final String finalId = id;
                        ImageView excluir = (ImageView) convertViewR.findViewById(R.id.excluirProd);
                        excluir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Model prodTask = new Model();
                                final String method = "http://192.168.0.85/erp/vendas_pedidos/excluirProduto";
                                final String function = "produto";
                                prodTask.execute(function, method,finalId, user_id,user_hash,"prod_id_principal");

                                Intent in = new Intent(mContext, CheckOutActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                mContext.startActivity(in);
                                ((CheckOutActivity) mContext).overridePendingTransition(R.anim.anim_fade_in,R.anim.anim_fade_in);
                                ((CheckOutActivity) mContext).finish();
                        }
                    });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }

                GridView grid = (GridView) convertViewR.findViewById(R.id.gridVariantes);
                SplittedGridVariantes gridVariantes = new SplittedGridVariantes(mContext,splittedVar);
                grid.setAdapter(gridVariantes);

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

        return convertViewR;
    }

}