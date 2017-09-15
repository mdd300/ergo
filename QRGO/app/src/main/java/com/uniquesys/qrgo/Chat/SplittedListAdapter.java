package com.uniquesys.qrgo.Chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uniquesys.qrgo.Produtos.GridProdutos.GridActivity;
import com.uniquesys.qrgo.Produtos.Model;
import com.uniquesys.qrgo.Produtos.ProdutoActivity;
import com.uniquesys.qrgo.R;
import com.uniquesys.qrgo.model.Imagem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class SplittedListAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<String> data;
    private int resource;
    private LayoutInflater inflater;
    ArrayList <String> layoutdef;

    public SplittedListAdapter(Context c, ArrayList<String> splittedMensagem, ArrayList<String> layout) {

        mContext = c;
        data=splittedMensagem;
        layoutdef = layout;
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

        final RelativeLayout convertViewR;

        String procurarPor = "#Produto#QRGO2017#";
        if (!data.get(position).contains(procurarPor)) {
            if(layoutdef.get(position) == "destinatario")
                resource = R.layout.mensagem_destinatario;
            else
                resource = R.layout.mensagem_remetente;


            convertViewR = (RelativeLayout) inflater.inflate(resource, null);

            TextView textView = (TextView) convertViewR.findViewById(R.id.Mensagem);
            textView.setText("    " + data.get(position).toString() + "    ");
        }
        else {
            final String[] separated = data.get(position).split("#Produto#QRGO2017#");
            if (layoutdef.get(position) == "destinatarioProd")
                resource = R.layout.mensagem_destinatario_prod;
            else
                resource = R.layout.mensagem_remetente_prod;

            convertViewR = (RelativeLayout) inflater.inflate(resource, null);

            ImageView Imagem = (ImageView) convertViewR.findViewById(R.id.ProdImagem);

            final String codigo = separated[1];
            String method = "http://uniquesys.jelasticlw.com.br/qrgo/pedidos/readqrcodepedido_app";
            String function = "produto";
            Model prodTask = new Model(mContext);
            prodTask.execute(function,method, codigo);
            String resultado = null;
            Log.e("prod",data.get(position));
            try {
                resultado = prodTask.get();

                final String finalResultado = resultado;
                convertViewR.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        Intent in = new Intent(mContext, ProdutoActivity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        in.putExtra("id", separated[1]);
                        in.putExtra("resultado", finalResultado);
                        mContext.startActivity(in);
                        ((ConversaActivity) mContext).overridePendingTransition(R.anim.anim_slide_right,R.anim.anim_slide_left);
                        ((ConversaActivity) mContext).finish();

                    }
                });


                Log.e("prod",resultado);
                JSONArray JASresult = new JSONArray(resultado.toString());
                JSONObject obj = JASresult.getJSONObject(0);
                String imgJ = obj.getString("image_thumb");

                String urlOfImage = "http://uniquesys.jelasticlw.com.br/qrgo/uploads/produtos/img/" + imgJ;
                method = urlOfImage;
                function = "imagem";
                com.uniquesys.qrgo.model.Imagem imgTask = new Imagem();
                imgTask.execute(function,method);
                Bitmap result = null;
                try {
                    result = imgTask.get();

                    int size = Math.min(result.getWidth(), result.getHeight());
                    int x = (result.getWidth() - size) / 2;
                    int y = (result.getHeight() - size) / 2;

                    Bitmap squared = Bitmap.createBitmap(result, x, y, size, size);


                    result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

                    Canvas canvas = new Canvas(result);
                    Paint paint = new Paint();
                    paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
                    paint.setAntiAlias(true);
                    float r = size / 2f;
                    canvas.drawCircle(r, r, r, paint);


                    ImageView ImagemV = (ImageView) convertViewR.findViewById(R.id.ProdImagem);
                    ImagemV.setImageBitmap(result);




                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        return convertViewR;
    }


}