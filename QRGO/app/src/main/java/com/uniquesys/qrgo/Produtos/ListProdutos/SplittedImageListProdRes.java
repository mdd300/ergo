package com.uniquesys.qrgo.Produtos.ListProdutos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.Enviar_mensagem_prod;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SplittedImageListProdRes extends BaseAdapter {

    Context mContext;
    List<Bitmap> data;
    List<String> id;
    private int resource;
    private LayoutInflater inflater;
    List<String> ListaContatos;
    String user_id;
    String hash;

    public SplittedImageListProdRes(Context c, String user,String s, List<String> contatos, List<Bitmap> splittedBitmaps, List<String> splittedid) {

        mContext = c;
        data=splittedBitmaps;
        id = splittedid;
        resource = R.layout.listview_fragment_layout;
        inflater = LayoutInflater.from(mContext);
        ListaContatos = contatos;
        user_id = user;
        hash = s;
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
        String method = "http://192.168.0.85/erp/vendas_produtos/getList";
        String function = "produto";
        Model prodTask = new Model(mContext);
        prodTask.execute(function,method, codigo,user_id,hash,"prod_id");
        try {
            final String resultado = prodTask.get();
            JSONArray JASresult = new JSONArray(resultado.toString());
            JSONObject obj = JASresult.getJSONObject(0);
            String nome = obj.getString("prod_desc");
            String preco = obj.getString("prod_preco");

            TextView Preco = (TextView) convertViewR.findViewById(R.id.txtPreco);
            TextView Ref = (TextView) convertViewR.findViewById(R.id.txtRef);
            TextView Nome = (TextView) convertViewR.findViewById(R.id.nome);
            TextView Prazo = (TextView) convertViewR.findViewById(R.id.ult_mes);

            Nome.setText(nome);
            Preco.setText(preco);


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

            ImageButton compartilhar = (ImageButton) convertViewR.findViewById(R.id.btnCompartilhar);
            compartilhar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                    builderSingle.setTitle("Select One Name:-");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.select_dialog_singlechoice);

                    for (int i=0;i<ListaContatos.size();i++){
                        arrayAdapter.add(ListaContatos.get(i));
                    }

                    builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);

                            String[] separated = strName.split(" - ");

                            Enviar_mensagem_prod enviar = new Enviar_mensagem_prod();
                            enviar.enviar_mensagem(separated[0],user_id,id.get(position));

                            AlertDialog.Builder builderInner = new AlertDialog.Builder(mContext);
                            builderInner.setTitle("O produto foi enviado com sucesso");
                            builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,int which) {
                                    dialog.dismiss();
                                }
                            });
                            builderInner.show();
                        }
                    });
                    builderSingle.show();
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